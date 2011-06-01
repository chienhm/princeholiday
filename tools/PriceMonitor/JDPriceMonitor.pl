#!/usr/bin/perl

#============================
# Head File.
#============================
use strict;
use warnings;
use Encode;
use encoding 'utf8' , STDIN => 'gb2312', STDOUT => 'gb2312';
use LWP;

#============================
# Parameter.
#============================
my $Book_List_File_Path = "books.txt";
my $Jingdong_URL = "http://book.360buy.com/";
my $Discount_Threshold = 0.5; #50% cut off
my $Store_Info_URL = "http://price.360buy.com/stocksoa/StockHandler.ashx?callback=getProvinceStockCallback&type=provincestock&provinceid=2&skuid=";


#============================
# Global Variable.
#============================
my %bookDiscountPrice;
my %bookOriginalPrice;
my %bookName;
my %Store_Info = ("0"=>"统计中", "33"=>"现货", "34"=>"无货", "36"=>"预定", "39"=>"在途", "40"=>"调配");

#============================
# Constant Varible.
#============================
my $BOOK_NAME_REX = "<title>《(.*)》（(.*)）.*</title>";
my $ORG_PRICE_REX = "价：<del>￥([0-9\.]+)</del></li>";
my $PRICE_URL_REX = "src=\"(http:\/\/price\.360buy\.com\/price-b-P([^\"]+).html)\"";
my $DIS_PRICE_REX = "\"\\\\uFFE5([0-9\.]+)\"";
my $STORE_INFO_REX = "stock:\{\"StockState\":([0-9]+),";


#============================
sub readBookList
{
    my $filePath = $_[0];
    open(BOOKLIST, $filePath);
    my $line;
    my @bookInfo;
    my $bookID;
    my $bookPrice;
    while ($line = <BOOKLIST>)
    {
        chomp($line);
        if (($line =~ m/^#/) || ($line =~ m/^\s*$/))
        {
            next;
        }
        else
        {
            @bookInfo = split(/\s+/, $line);
            $bookID = $bookInfo[0];
            $bookDiscountPrice{$bookID} = $bookInfo[2];
            #print "$bookID, $bookPrice\n";
        }
    }
    close(BOOKLIST);
}

#============================
sub notifyBookPrice
{
    my $id = $_[0];
    my $name = $_[1];
    my $orgPrice = $_[2];
    my $disPrice = $_[3];
    my $storeInfo = $_[4];
    my $discount = int($disPrice/$orgPrice*100+0.5);
    # send a message
    print "$name, 原价:$orgPrice, 京东价:$disPrice, 折扣:$discount%, $Store_Info{$storeInfo}!\n";
}

#============================
sub monitorBookPrice
{
    my $bookURL;
    my $userAgent = LWP::UserAgent->new;
    my $response;
    my $page;
    my $bookID;
    my $bookSID;
    my $orgPrice;
    my $lastPrice;
    my $discountPriceURL;
    my $discountPrice;
    my $storeInfo;
    foreach $bookID (keys %bookDiscountPrice)
    {
        $bookURL = $Jingdong_URL.$bookID.".html";
        #print "$bookURL\n";
        $response = $userAgent->get($bookURL);
        if ($response->is_success)
        {
            $page = $response->decoded_content;
            if ($page =~ m/$BOOK_NAME_REX/)
            {
                $bookName{$bookID} = $1."(".$2.")";
                #print "$bookName{$bookID}\n";
                if ($page =~ m/$ORG_PRICE_REX/)
                {
                    $orgPrice = $1;
                    $bookOriginalPrice{$bookID} = $orgPrice;
                    #print "$orgPrice\n";
                    if (undef($bookDiscountPrice{$bookID}))
                    {
                        $bookDiscountPrice{$bookID} = $orgPrice;
                    }
                    if ($page =~ m/$PRICE_URL_REX/)
                    {
                        $discountPriceURL = $1;
                        $bookSID = $2;
                        #print "$discountPriceURL, $bookSID\n";
                        $response = $userAgent->get($discountPriceURL);
                        $page = $response->decoded_content;
                        if ($page =~ m/$DIS_PRICE_REX/)
                        {
                            $discountPrice = $1;
                            #print "$discountPrice\n";
                            $lastPrice = $bookDiscountPrice{$bookID};
                            $bookDiscountPrice{$bookID} = $discountPrice;
                            if (($discountPrice/$orgPrice <= $Discount_Threshold))
                                # && ($discountPrice < $lastPrice))
                            {
                                $storeInfo = 0;
                                $response = $userAgent->get($Store_Info_URL.$bookSID);
                                $page = $response->decoded_content;
                                if ($page =~ m/$STORE_INFO_REX/)
                                {
                                    $storeInfo = $1;
                                }
                                notifyBookPrice($bookID, $bookName{$bookID}, $orgPrice, $discountPrice, $storeInfo);
                            }
                        }
                    }
                }
            }
        }
    }
}

sub writeBookList
{
    my $filePath = $_[0];
    my $bookID;
    my $discount;
    open(BOOKLIST, ">$filePath");
    foreach $bookID (keys %bookDiscountPrice)
    {
        print BOOKLIST encode("utf8", "# $bookName{$bookID}\n");
        $discount = int($bookDiscountPrice{$bookID}/$bookOriginalPrice{$bookID}*100+0.5);
        print BOOKLIST "$bookID $bookOriginalPrice{$bookID} $bookDiscountPrice{$bookID} $discount\%\n";
    }
    close(BOOKLIST);
}

sub main
{
    readBookList($Book_List_File_Path);
    monitorBookPrice();
    writeBookList($Book_List_File_Path);
}

main;