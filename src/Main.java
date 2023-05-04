import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress user1 = new GameProgress(60, 4, 2, 30.0);
        GameProgress user2 = new GameProgress(80, 9, 1, 90.0);
        GameProgress user3 = new GameProgress(20, 2, 5, 10.0);

        String filePathUser1 = "C:\\Users\\Lisitsa Vera\\java\\Games\\saveGames\\saveUser1.dat";
        String filePathUser2 = "C:\\Users\\Lisitsa Vera\\java\\Games\\saveGames\\saveUser2.dat";
        String filePathUser3 = "C:\\Users\\Lisitsa Vera\\java\\Games\\saveGames\\saveUser3.dat";

        saveGame(filePathUser1, user1);
        saveGame(filePathUser2, user2);
        saveGame(filePathUser3, user3);

        String zipPath = "C:\\Users\\Lisitsa Vera\\java\\Games\\saveGames\\zip.zip";
        List<String> filePaths = new ArrayList<>();
        filePaths.add(filePathUser1);
        filePaths.add(filePathUser2);
        filePaths.add(filePathUser3);

        zipFiles(zipPath, filePaths);

        String dirPath = "C:\\Users\\Lisitsa Vera\\java\\Games\\saveGames";

        openZip(zipPath, dirPath);

        for (String filePath : filePaths) {
            System.out.println(openProgress(filePath));
        }


    }

    public static void saveGame(String filePath, GameProgress user) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void zipFiles(String zipPath, List<String> filePaths) {
        try {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath));
            Iterator<String> iterator = filePaths.iterator();
            while (iterator.hasNext()) {
                String file = iterator.next();
                File newFile = new File(file);
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(newFile.getName());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    newFile.delete();
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                newFile.delete();
            }
            zout.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void openZip(String zipPath, String dirPath) {
        try (ZipInputStream zin = new ZipInputStream(new
                FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                File file = new File(dirPath, name);
                FileOutputStream fout = new FileOutputStream(file.getPath());
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
    }

    public static GameProgress openProgress(String FilePath) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(FilePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }
}