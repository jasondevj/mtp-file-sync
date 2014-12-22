package dev.j.mtp;


import jpmp.device.UsbDevice;
import jpmp.manager.DeviceManager;

import java.util.Iterator;

/**
 * Created by Jason on 4/12/2014.
 */
public class SystemStartup {

    private DeviceManager dm;

    public static void main(String[] args) throws Throwable {
        if (args.length < 2) {
            System.out.println("Please specify the paths as following <the target location> <source location>");
            System.out.println("In case there is a intermediate directory that you want to remove from the destination directory while copying the files, you can specify it as the third parameter");
            System.exit(-1);
        }
        new SystemStartup().start(args);
    }

    public Integer start(String[] args) {
        try {
            System.out.println("Starting the sync...");
            System.out.println("Destination Path [" + args[0] + "] Source Path [" + args[1] + "]");
            dm = DeviceManager.getInstance();
            dm.createInstance();
            dm.scanDevices();
            if (dm.getDeviceList() != null && dm.getDeviceList().size() > 0) {
                UsbDevice usbDevice;
                Iterator it = dm.getDeviceList().keySet().iterator();
                String devkey = (String) it.next();
                usbDevice = (UsbDevice) dm.getDeviceList().get(devkey);
                System.out.println(usbDevice.getName());
                MtpFileService mtpFileService = new MtpFileService(args[0], args[1], usbDevice);
                if (args.length > 2) {
                    System.out.println("Filter Name [" + args[2] + "]");
                    mtpFileService.setDestinationTrim(args[2]);
                }
                mtpFileService.copyFilesToComputer();
            } else {
                System.out.println("No device was found to start the sync");
            }
            System.out.println("Sync Completed");
        } catch (Throwable throwable) {
            System.out.println("Error : " + throwable.getMessage());
            throw new RuntimeException(throwable);
        }finally {
            dm.releaseInstance();
        }

        return null;
    }
}