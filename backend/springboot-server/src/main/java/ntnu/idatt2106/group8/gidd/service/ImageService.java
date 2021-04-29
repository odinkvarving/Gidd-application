package ntnu.idatt2106.group8.gidd.service;

import ntnu.idatt2106.group8.gidd.model.entities.AccountInfo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

/**
 * A image service for adding images to users in the database.
 *
 * @author Endré Hadzalic
 */

@Service
public class ImageService {
    public static final String STORAGE_DIR = System.getProperty("user.dir") + "\\src\\main\\resources\\profilepictures";
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Autowired
    AccountService accountService;

    /**
     * Uploads a image to the server and stores the url to the picture in a account.
     *
     * @param file      the image to store to a account.
     * @param accountId the id of the account to store the image to
     * @return
     */
    public boolean uploadPictureToAccount(MultipartFile file, int accountId) {

        if (file == null) {
            logger.info("a null element was passed as file when trying to upload picture to account");
            return false;
        }
        String fileExtension = "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path storageDirectory = Paths.get(STORAGE_DIR);

        if (!Files.exists(storageDirectory)) { // if the folder does not exist
            try {
                Files.createDirectories(storageDirectory); // we create the directory in the given storage directory path
            } catch (Exception e) {
                logger.error("something went wrong while trying to create imagefolder", e);
                return false;
            }
        }
        String newFileName = generateRandomAlphanumericString(50) + fileExtension;
        while (new File(STORAGE_DIR + "\\" + newFileName).exists()) {
            newFileName = generateRandomAlphanumericString(50) + fileExtension;
        }


        Path destination = Paths.get(STORAGE_DIR + "\\" + newFileName);

        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);// we are Copying all bytes from an input stream to a file
        } catch (IOException e) {
            logger.error("something went wrong while trying to copy the file", e);
            return false;
        }

        AccountInfo info = this.accountService.findAccountInfo(accountId);

        if (info != null) {
            info.setImageURL("http://localhost:8080/profilepictures/" + newFileName);
            this.accountService.saveAccountInfoToAccount(info, accountId);
        } else {
            logger.info("The account has no info or does not exist");
            return false;
        }


        logger.info("sucsessfuly saved: " + storageDirectory + "\\" + newFileName + " to account: " + accountId);
        return true;
    }

    private String generateRandomAlphanumericString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Getter for a image that is stored in the database.
     *
     * @param imageName the name of the image to find.
     * @return the image as a bytearray, returns the default avatar if the image was not found.
     * @throws IOException
     */
    public byte[] getImageWithMediaType(String imageName) {
        try {
            if (new File(STORAGE_DIR + "\\" + imageName).exists()) {
                Path destination = Paths.get(STORAGE_DIR + "\\" + imageName);// retrieve the image by its name
                logger.info("serving : " + destination);
                return IOUtils.toByteArray(destination.toUri());
            } else {
                Path destination = Paths.get(STORAGE_DIR + "\\" + "default-avatar.png");
                logger.info(imageName + " was not found, returning default avatar.");
                return IOUtils.toByteArray(destination.toUri());
            }
        } catch (IOException ioe) {
            logger.error("Error during fetching of image.", ioe);
            return null;
        }
    }
}
