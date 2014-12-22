/*
 * Copyright (c) 2014. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package dev.j.mtp;

import jpmp.device.UsbDevice;
import jpmp.notifier.IDeviceTransferNotifier;
import jpmp.notifier.IParseTreeNotifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MTP management class
 */
public class MtpFileService {

    public static final String PATH_TERMINATOR = "/";
    private final String syncDestination;
    private final String syncSource;
    private String destinationTrim;
    private float totalCount = 0;
    private float currentCount = 0;
    private IDeviceTransferNotifier iDeviceTransferNotifier;
    private final HashMap<String, List<String>> fileMap;
    private UsbDevice usbDevice;

    /**
     * Create a MTPFileService class which would be fixed for a sync location
     * @param syncDestination Location of the destination directory in the PC
     * @param syncSource Location of the source directory on the MTP device
     * @param usbDevice USB Device instance to load the files from
     */
    public MtpFileService(String syncDestination, String syncSource, UsbDevice usbDevice) {
        this.syncDestination = syncDestination.trim();
        this.syncSource = syncSource.trim();
        this.usbDevice = usbDevice;
        fileMap = new HashMap<>();
        fileMap.put(syncSource, new ArrayList<>());

        iDeviceTransferNotifier = new IDeviceTransferNotifier() {
            @Override
            public void notifyBegin(long l) {

            }

            @Override
            public void notifyCurrent(long l) {
            }

            @Override
            public void notifyEnd() {
                System.out.println("Total Completed : " + calculateCompletedPercentage() + "%");
            }

            @Override
            public boolean getAbort() {
                return false;
            }
        };
    }

    private float loadFileList(String path) {
        usbDevice.parseFolder(path, new IParseTreeNotifier() {
            public long addFolder(String name, String objId) {
                String subPath = path + PATH_TERMINATOR + name;
                if (!fileMap.containsKey(subPath))
                    fileMap.put(subPath, new ArrayList<>());
                System.out.println("Loading files in [" + subPath + "]");
                loadFileList(subPath);
                return 0;
            }

            public long addFile(String name, String objId) {
                name = PATH_TERMINATOR + name;
                fileMap.get(path).add(name);
                totalCount++;
                return 0;
            }
        });
        return totalCount;
    }


    public synchronized void copyFilesToComputer() {
        currentCount = 0;
        totalCount = 0;
        System.out.println("Starting to load files...");
        loadFileList(syncSource);
        System.out.println("Load completed");
        for (Map.Entry<String, List<String>> entry : fileMap.entrySet()) {
            entry.getValue().forEach(fileName -> {
                String newKey;
                if (destinationTrim != null) {
                    newKey = entry.getKey().replace(destinationTrim, "");
                } else {
                    newKey = entry.getKey();
                }
                String destinationDir = syncDestination + newKey.trim();
                try {
                    Files.createDirectories(Paths.get(destinationDir));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String fileToCopy = entry.getKey() + fileName;
//                System.out.println("Copying File [" + fileToCopy + "] to [" + destinationDir + "]");
                if (!Files.exists(Paths.get(destinationDir + fileName))) {
                    usbDevice.getFile(destinationDir + fileName, fileToCopy, iDeviceTransferNotifier);
                } else {
                    System.out.println("File already exists, ignoring. Total Completed : " + calculateCompletedPercentage() + "%");
                }
            });
        }
    }


    private long calculateCompletedPercentage() {
        float f = ++currentCount / totalCount;
        return (long) (f * 100);
    }

    /**
     * In case there is a intermediate directory that you want to remove from the destination directory while copying the files
     * You can mention it here.
     *
     * Example :
     *  syncDestination = c:/sync
     *  syncSource      = /Gallery/images
     *
     *  With filter disabled all files inside syncSource will be copied to c:/sync/Gallery/images
     *  If filter is set as '/Gallery' files will be copied to c:/sync/images
     *
     * @param destinationTrim
     */
    public void setDestinationTrim(String destinationTrim) {
        this.destinationTrim = destinationTrim;
    }
}
