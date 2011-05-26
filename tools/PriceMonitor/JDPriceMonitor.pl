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


#============================
# Global Variable.
#============================
my %bookDiscountPrice;
my %bookName;

#
# Constant Varible.
#
my $BOOK_NAME_REX = "<title>《(.*)》（(.*)）.*</title>";
my $ORG_PRICE_REX = "<li>定&nbsp;&nbsp;&nbsp;&nbsp;价：<del>￥([0-9\.]+)</del></li>";
my $PRICE_URL_REX = "src=\"(http:\/\/price\.360buy\.com\/price-b-[^\"]+)\"";
my $DIS_PRICE_REX = "\"\\\\uFFE5([0-9\.]+)\"";


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
        if (($line =~ m/^\#/) || ($line =~ m/^\s*$/))
        {
            next;
        }
        else
        {
            @bookInfo = split(/\s+/, $line);
            $bookID = $bookInfo[0];
            $bookPrice = $bookInfo[1];
            $bookDiscountPrice{$bookID} = $bookPrice;
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
    # send a message
    print "Book:$name, Original Price:$orgPrice, Discount Price:$disPrice!\n";
}

#============================
sub monitorBookPrice
{
    my $bookURL;
    my $userAgent = LWP::UserAgent->new;
    my $response;
    my $page;
    my $bookID;
    my $orgPrice;
	my $lastPrice;
    my $discountPriceURL;
    my $discountPrice;
    foreach $bookID (keys %bookDiscountPrice)
    {
        $bookURL = $Jingdong_URL.$bookID.".html";
        $response = $userAgent->get($bookURL);
        if ($response->is_success)
        {
            $page = $response->decoded_content;
            if ($page =~ m/$BOOK_NAME_REX/)
            {
                $bookName{$bookID} = $1."(".$2.")";
                if ($page =~ m/$ORG_PRICE_REX/)
                {
                    $orgPrice = $1;
                    if ($page =~ m/$PRICE_URL_REX/)
                    {
                        $discountPriceURL = $1;
                        $response = $userAgent->get($discountPriceURL);
                        $page = $response->decoded_content;
                        if ($page =~ m/$DIS_PRICE_REX/)
                        {
                            $discountPrice = $1;
							$lastPrice = $bookDiscountPrice{$bookID};
							$bookDiscountPrice{$bookID} = $discountPrice;
                            if (($discountPrice/$orgPrice <= $Discount_Threshold) 
                                && ($discountPrice < $lastPrice))
                            {
                                notifyBookPrice($bookID, $bookName{$bookID}, $orgPrice, $discountPrice);
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
    open(BOOKLIST, ">$filePath");
    foreach $bookID (keys %bookDiscountPrice)
    {
        print BOOKLIST encode("utf8", "# $bookName{$bookID}\n");
        print BOOKLIST "$bookID $bookDiscountPrice{$bookID}\n";
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