package com.example.examplemod.api.misc;

import com.example.examplemod.data.WirelessEnergySavaedData;

import java.math.BigInteger;
import java.util.UUID;

import static com.example.examplemod.api.misc.GlobalVariableStorage.GlobalEnergy;

public class WirelessEnergyManager {

    public static void strongCheckOrAddUser(UUID user_uuid) {

        if (!GlobalEnergy.containsKey(user_uuid)) {
            GlobalEnergy.put(user_uuid, BigInteger.ZERO);
        }
    }

    public static boolean addEUToGlobalEnergyMap(UUID user_uuid, BigInteger EU) {
        // Mark the data as dirty and in need of saving.
        try {
            WirelessEnergySavaedData.INSTANCE.setDirty(true);

        } catch (Exception exception) {
            System.out.println("COULD NOT MARK GLOBAL ENERGY AS DIRTY IN ADD EU");
            exception.printStackTrace();
        }

        // Get the team UUID. Users are by default in a team with a UUID equal to their player UUID.
        //UUID teamUUID = SpaceProjectManager.getLeader(user_uuid);

        // Get the teams total energy stored. If they are not in the map, return 0 EU.
        BigInteger totalEU = GlobalEnergy.getOrDefault(user_uuid, BigInteger.ZERO);
        totalEU = totalEU.add(EU);

        // If there is sufficient EU then complete the operation and return true.
        if (totalEU.signum() >= 0) {
            GlobalEnergy.put(user_uuid, totalEU);
            //WirelessEnergySavaedData.INSTANCE.updateEnergy(user_uuid,totalEU.longValue());
            return true;
        }

        // There is insufficient EU so cancel the operation and return false.
        return false;
    }

    public static boolean addEUToGlobalEnergyMap(UUID user_uuid, long EU) {
        return addEUToGlobalEnergyMap(user_uuid, BigInteger.valueOf(EU));
    }

    public static boolean addEUToGlobalEnergyMap(UUID user_uuid, int EU) {
        return addEUToGlobalEnergyMap(user_uuid, BigInteger.valueOf(EU));
    }

    // ------------------------------------------------------------------------------------

    public static BigInteger getUserEU(UUID user_uuid) {
        return GlobalEnergy.getOrDefault(user_uuid, BigInteger.ZERO);
    }

    // This overwrites the EU in the network. Only use this if you are absolutely sure you know what you are doing.
    public static void setUserEU(UUID user_uuid, BigInteger EU) {
        // Mark the data as dirty and in need of saving.
        try {
            WirelessEnergySavaedData.INSTANCE.setDirty(true);
        } catch (Exception exception) {
            System.out.println("COULD NOT MARK GLOBAL ENERGY AS DIRTY IN SET EU");
            exception.printStackTrace();
        }

        GlobalEnergy.put(user_uuid, EU);
    }

    public static void clearGlobalEnergyInformationMaps() {
        // Do not use this unless you are 100% certain you know what you are doing.
        GlobalEnergy.clear();
    }

}
