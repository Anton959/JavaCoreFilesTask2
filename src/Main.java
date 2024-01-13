import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(1, 1, 1, 1.1);
        String gameProgress1Address = "C:\\Users\\Администратор\\Desktop\\Games\\saveGames\\save1.dat";
        GameProgress gameProgress2 = new GameProgress(2, 2, 2, 2.2);
        String gameProgress2Address = "C:\\Users\\Администратор\\Desktop\\Games\\saveGames\\save2.dat";
        GameProgress gameProgress3 = new GameProgress(3, 3, 3, 3.3);
        String gameProgress3Address = "C:\\Users\\Администратор\\Desktop\\Games\\saveGames\\save3.dat";

        saveGames(gameProgress1, gameProgress1Address);
        saveGames(gameProgress2, gameProgress2Address);
        saveGames(gameProgress3, gameProgress3Address);

        List<String> zipAdressGame = Arrays.asList(gameProgress1Address, gameProgress2Address, gameProgress3Address);
        String zipAddressArchive = "C:\\Users\\Администратор\\Desktop\\Games\\saveGames\\zip.zip";

        zipFile(zipAddressArchive, zipAdressGame);
    }

    public static void saveGames(GameProgress gameProgress, String address) {
        try (FileOutputStream fileOutputStream =
                     new FileOutputStream(address);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(gameProgress);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void zipFile(String zipAddress, List<String> gameAddress) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipAddress))) {
            for (int i = 0; i < gameAddress.size(); i++) {
                try (FileInputStream fis = new FileInputStream(gameAddress.get(i))) {
                    ZipEntry entry = new ZipEntry(gameAddress.get(i));
                    zout.putNextEntry(entry);
                    //считываем содержимое файла в массив byte
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    // Добавляем содержимое к архиву
                    zout.write(buffer);
                    // Закрываем текущую запись для новой записи
                    zout.closeEntry();

                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
                Path path = Paths.get(gameAddress.get(i));
                delete(path);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void delete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}