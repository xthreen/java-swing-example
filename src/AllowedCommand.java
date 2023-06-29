import java.util.Arrays;

public enum AllowedCommand {
    ADB_START_SERVER("adb", "start-server"),
    ADB_DEVICES("adb", "devices", "-l"),
    ADB_KILL_SERVER("adb", "kill-server"),
    ADB_REBOOT_BOOTLOADER("adb", "reboot", "bootloader"),
    ADB_REBOOT_RECOVERY("adb", "reboot", "recovery"),
    ADB_SHELL_GETPROP_PRODUCT("adb", "shell", "getprop", "ro.product.product.device"),
    ADB_SHELL_GETPROP_OEM_UNLOCK("adb", "shell", "getprop", "sys.oem_unlock_allowed"),
    ADB_FASTBOOT_FLASH("fastboot", "flash"),
    FASTBOOT_FLASHING_UNLOCK("fastboot", "flashing", "unlock"),
    FASTBOOT_FLASHING_LOCK("fastboot", "flashing", "lock"),
    FASTBOOT_SET_ACTIVE_A("fastboot", "set_active", "a");

    private final String[] args;
    AllowedCommand(String... args) {
        this.args = args;
    }
    public String[] getCommand() {
        return this.args;
    }

    public static AllowedCommand tryCommand(String[] command) throws IllegalArgumentException {
        for (AllowedCommand allowedCommand : AllowedCommand.values()) {
            if (Arrays.equals(allowedCommand.getCommand(), command)) {
                return allowedCommand;
            }
        }
        throw new IllegalArgumentException("Received illegal input: command and/or args not recognized.");
    }
}
