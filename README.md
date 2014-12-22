MTP File Sync
=============

Small program to sync changed files in a directory of a MTP devices such as Android, iPhone, ext.. and a computer directory

How To Run
----------

<h2>./start.bat (Destination Location) (Source Path) (Filter Directory)</h2>

    Example 1: ./start.bat "c:/sync-path" "/Gallery"

    Example 2: ./start.bat "c:/sync-path" "/Gallery" "/Gallery"
        (Destination Location) = "c:/sync-path"
        (Source Path)          = "/Gallery/images"
         With (Filter Directory) not set all files inside (Destination Location) will be copied to c:/sync-path/Gallery/images
         If (Filter Directory) is set as '/Gallery' files will be copied to c:/sync-path/images

    (Destination Location) : Location on your PC
    (Source Path) : Location on the Device
    (Filter Directory) : In case there is a intermediate directory that you want to remove from the destination directory while copying the files


