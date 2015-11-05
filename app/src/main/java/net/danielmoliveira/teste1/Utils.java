package net.danielmoliveira.teste1;

import stone.utils.GlobalInformations;

/**
 * Created by JGabrielFreitas on 30/10/15.
 */
public class Utils {

    public static boolean isConnectedWithPinpad(){
        return GlobalInformations.getPinpadListSize() != null && GlobalInformations.getPinpadListSize() > 0;
    }
}
