import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class HWIDExample {

    /**
     * Ana uygulama sınıfı. Donanım kimliklerini alır ve konsola yazdırır.
     */
    public static void main(String[] args) {
        // Ana kart kimliğini al
        String motherboardID = getHardwareID("Motherboard");

        // İşlemci kimliğini al
        String cpuID = getHardwareID("CPU");

        // Sabit disk kimliğini al
        String diskID = getHardwareID("Disk");

        // Ayrı ayrı alınan HWID'leri konsola yazdır
        System.out.println("Motherboard ID: " + motherboardID);
        System.out.println("CPU ID: " + cpuID);
        System.out.println("Disk ID: " + diskID);
    }

    /**
     * Belirtilen donanım türü için donanım kimliğini alır.
     *
     * @param hardwareType Donanım türü (örneğin, "Motherboard", "CPU", "Disk")
     * @return Donanım kimliği
     */
    private static String getHardwareID(String hardwareType) {
        try {
            Process process;
            String os = System.getProperty("os.name").toLowerCase();

            // İşletim sistemine göre farklı komutlarla işlem yapılır
            if (os.contains("win")) {
                process = Runtime.getRuntime().exec(new String[]{"wmic", hardwareType, "get", "SerialNumber"});
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                String command;
                if (hardwareType.equals("CPU")) {
                    // Linux ve benzeri sistemlerde işlemci kimliği algoritması
                    command = "cat /proc/cpuinfo | grep 'Serial' | awk '{print $3}'";
                } else {
                    // Linux ve benzeri sistemlerde sabit disk kimliği algoritması
                    command = "lsblk -no serial /dev/sda";
                }
                process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            } else {
                throw new UnsupportedOperationException("Unsupported operating system");
            }

            process.getOutputStream().close();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        return line.trim();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

/**
 * Ek main sınıfı, HWIDExample sınıfını kullanarak donanım kimliklerini alır ve konsola yazdırır.
 */
class MainClass {
    public static void main(String[] args) {
        // Ana kart kimliğini al
        String motherboardID = HWIDExample.getHardwareID("Motherboard");

        // İşlemci kimliğini al
        String cpuID = HWIDExample.getHardwareID("CPU");

        // Sabit disk kimliğini al
        String diskID = HWIDExample.getHardwareID("Disk");

        // Ayrı ayrı alınan HWID'leri konsola yazdır
        System.out.println("Motherboard ID: " + motherboardID);
        System.out.println("CPU ID: " + cpuID);
        System.out.println("Disk ID: " + diskID);
    }
}
