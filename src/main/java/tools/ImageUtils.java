package tools;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();

    /**
     * 🔥 Charge une image, la redimensionne et la crop pour qu'elle soit exactement de la taille demandée.
     */
    public static ImageIcon loadAndResizeImage(String path, int targetWidth, int targetHeight) {
        // Inclure les dimensions dans la clé pour permettre le caching de plusieurs tailles
        String key = path + "_" + targetWidth + "x" + targetHeight;
        if (imageCache.containsKey(key)) {
            return imageCache.get(key);
        }

        try {
            BufferedImage originalImage = ImageIO.read(ImageUtils.class.getResource(path));
            if (originalImage == null) {
                return null;
            }
            BufferedImage resizedImage = resizeAndCropImage(originalImage, targetWidth, targetHeight);
            ImageIcon imageIcon = new ImageIcon(resizedImage);
            imageCache.put(key, imageIcon); // Mise en cache avec la clé complète
            return imageIcon;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("❌ Impossible de charger l'image : " + path);
            return null;
        }
    }


    /**
     * 🔄 Redimensionne et crop l'image pour qu'elle soit exactement de la taille demandée.
     */
    private static BufferedImage resizeAndCropImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 🔄 Calcul du facteur d'échelle
        double scaleX = (double) targetWidth / originalWidth;
        double scaleY = (double) targetHeight / originalHeight;
        double scale = Math.max(scaleX, scaleY); // Assure que l'image couvre bien toute la zone

        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        // ✅ On s'assure que l'image est suffisamment grande
        if (newWidth < targetWidth) newWidth = targetWidth;
        if (newHeight < targetHeight) newHeight = targetHeight;

        // 🔄 Redimensionner l'image
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();

        // ✅ Calcul sécurisé pour le crop (évite RasterFormatException)
        int cropX = Math.max(0, (newWidth - targetWidth) / 2);
        int cropY = Math.max(0, (newHeight - targetHeight) / 2);

        // ⚠️ Correction : Vérifier que le crop ne dépasse pas les limites
        int cropWidth = Math.min(targetWidth, newWidth - cropX);
        int cropHeight = Math.min(targetHeight, newHeight - cropY);

        return resizedImage.getSubimage(cropX, cropY, cropWidth, cropHeight);
    }

}
