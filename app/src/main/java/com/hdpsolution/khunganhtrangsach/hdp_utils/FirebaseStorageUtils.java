package com.hdpsolution.khunganhtrangsach.hdp_utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by a1 on 2/22/18.
 */

public class FirebaseStorageUtils {

    private static FirebaseStorage storage;
    private static StorageReference storageRef;

    private static void initStorageInstancesIfNeeded() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance();

            storage.setMaxOperationRetryTimeMillis(5000);
        }

        if (storageRef == null) {
            storageRef = storage.getReference();
        }

    }


    public static void uploadFileFromFileInputStream(String fileLocalPath, String fileRef) {

        initStorageInstancesIfNeeded();

        InputStream stream = null;
        try {
            stream = new FileInputStream(fileLocalPath);

            StorageReference storageReference = storageRef.child(fileRef);

            UploadTask uploadTask = storageReference.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads

                    Log.e("firebase", exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Log.e("firebase", downloadUrl.toString());

                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void uploadFileFromUriLocal(Uri uriLocal, String fileRef) {

        initStorageInstancesIfNeeded();

        StorageReference storageReference = storageRef.child(fileRef);
        UploadTask uploadTask = storageReference.putFile(uriLocal);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

                Log.e("firebase", exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                Log.e("firebase", downloadUrl.toString());
            }
        });
    }

    public static void checkIsFileRefExists(Context ctx, String fileRef, final Idelegate callback) {

        initStorageInstancesIfNeeded();

        StorageReference storageReference = storageRef.child(fileRef);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                callback.callBack(0, 1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // File not found
                callback.callBack(0, 0);
            }
        });

    }

    public static void displayImageFromFirebaseStorage(Context ctx, ImageView imageView, String fileRef, int resPlaceHolder) {

        initStorageInstancesIfNeeded();

        StorageReference storageReference = storageRef.child(fileRef);

        try {
            Glide.with(ctx)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .placeholder(resPlaceHolder)
                    .into(imageView);
        } catch (Exception e) { // fix crash report

        }

    }
    public static void displayImageFromFirebaseStorageasProcessbar(Context ctx, final ProgressBar pr, ImageView imageView, String fileRef, int resPlaceHolder) {

        initStorageInstancesIfNeeded();

        StorageReference storageReference = storageRef.child(fileRef);

        try {
            Glide.with(ctx)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .listener(new RequestListener<StorageReference, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                            pr.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            pr.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .crossFade(1000)
                    .into(imageView);
        } catch (Exception e) { // fix crash report

        }

    }
    public static void displayImageFromFirebaseStoragenoLoad(Context ctx, ImageView imageView, String fileRef, int resPlaceHolder) {

        initStorageInstancesIfNeeded();

        StorageReference storageReference = storageRef.child(fileRef);

        try {
            Glide.with(ctx)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .placeholder(resPlaceHolder)
                    .into(imageView);
        } catch (Exception e) { // fix crash report

        }

    }

    public static void displayImageFromFirebaseStorageToBitmap(Context ctx, RemoteViews mRemoteView, String fileRef, final Idelegate callback) {

        initStorageInstancesIfNeeded();

        StorageReference storageReference = storageRef.child(fileRef);

        try {
//            Glide.with(ctx)
//                    .using(new FirebaseImageLoader())
//                    .load(storageReference)
//                    .asBitmap()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                    .dontAnimate()
//                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                            resource = AppUtils.getCircularBitmap(resource);
//                            mRemoteView.setImageViewBitmap(R.id.img_avata, resource);
//                        }
//                    });


            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    // Pass it to Picasso to download, show in ImageView and caching

                    String link = uri.toString();

                    callback.callBack(link, 0);

//                    Picasso.with(ctx).load(link).placeholder(R.drawable.account).noFade().into(new com.squareup.picasso.Target() {
//                        @Override
//                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                            bitmap = AppUtils.getCircularBitmap(bitmap);
//                            mRemoteView.setImageViewBitmap(R.id.img_avata, bitmap);
//                        }
//
//                        @Override
//                        public void onBitmapFailed(Drawable errorDrawable) {
//
//                        }
//
//                        @Override
//                        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                        }
//                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } catch (Exception e) { // fix crash report

        }

    }


    public static String getFilePath(String name, String folderName) {
        String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        File pictureFolder = new File(picturePath);
        if (!pictureFolder.exists()) {
            boolean mkdir = pictureFolder.mkdir();
            if (!mkdir) return null;
        }
        File file = new File(picturePath + "/" + folderName);
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (!mkdir) return null;
        }
        return file.getPath() + "/" + name;
    }

    public static void deleteFile(String fileRef) {
        // Create a reference to the file to delete
        StorageReference mountainRef = storageRef.child(fileRef);

        // Delete the file
        mountainRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully

                Log.e("firebase", "delete successfull");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.e("firebase", exception.getMessage());
            }
        });
    }


    private void testGetRemoteConfigData() {

        storageRef.child("test/test.txt").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image - file ?

                try {
                    String data = new String(bytes, "utf-8").replace("\n", "").replace("\r", "").trim();

                    Log.e("read data", data);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("abc", exception.getMessage());
            }
        });
    }


//    huy

    public static void uploadFileFromUriLocal(Uri uriLocal, String fileRef, final OnUploadListener listener) {
        initStorageInstancesIfNeeded();

        StorageReference storageReference = storageRef.child(fileRef);
        UploadTask uploadTask = storageReference.putFile(uriLocal);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase", exception.getMessage());
                if (listener != null) listener.onFailure("");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.e("firebase", downloadUrl.toString());
                if (listener != null) listener.onSuccess(downloadUrl, 0);
            }
        });
    }

    public static void uploadFileFromUriLocalAndScale(Context context, Uri uriLocal, String fileRef, final OnUploadListener listener) {
        initStorageInstancesIfNeeded();

        Bitmap bitmap = BitmapUtils.decodeScaleBitmapFromPath(AppUtils.getRealPath(context, uriLocal.getPath()), 400, 400);
        if (bitmap != null) {
            Log.e("bitmapupload: ", bitmap.getWidth() + "==" + bitmap.getHeight());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            StorageReference storageReference = storageRef.child(fileRef);
            UploadTask uploadTask = storageReference.putBytes(byteArrayOutputStream.toByteArray());
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase", exception.getMessage());
                    if (listener != null) listener.onFailure("Failed to upload");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.e("firebase", downloadUrl.toString());
                    if (listener != null) listener.onSuccess(downloadUrl, 0);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    float progress = (100f * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    Log.e("onProgress: ", "Upload is " + progress + "% done");
                    if (listener != null) listener.onProgress(progress);
                }
            });
        } else {
//            Todo: show dialog cannot upload too large file
            if (listener != null)
                listener.onFailure("Failed to upload, image file is too large or corrupted");
        }


    }

    public static void uploadMultiFileFromUriLocal(List<String> paths, String fileRef, final OnUploadListener listener) {
        initStorageInstancesIfNeeded();

        for (int i = 0; i < paths.size(); i++) {
            StorageReference storageReference = storageRef.child(fileRef + System.currentTimeMillis() + ".png");
            UploadTask uploadTask = storageReference.putFile(Uri.parse(paths.get(i)));
            final int finalI = i;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase", exception.getMessage());
                    if (listener != null) listener.onFailure("");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.e("firebase", downloadUrl.toString());
                    if (listener != null) listener.onSuccess(downloadUrl, finalI);
                }
            });
        }
    }

    public static void uploadMultiFileFromUriLocalAndScale(Context context, List<String> paths, String fileRef, final OnUploadListener listener) {
        initStorageInstancesIfNeeded();

        for (int i = 0; i < paths.size(); i++) {

            String realPath = AppUtils.getRealPath(context, paths.get(i));
            Bitmap bitmap = BitmapUtils.decodeScaleBitmapFromPath(realPath, 400, 400);
            if (bitmap != null) {
                Log.e("bitmapupload: ", bitmap.getWidth() + "==" + bitmap.getHeight());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                StorageReference storageReference = storageRef.child(fileRef + System.currentTimeMillis() + ".png");
                UploadTask uploadTask = storageReference.putBytes(byteArrayOutputStream.toByteArray());
                final int finalI = i;
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("firebase", exception.getMessage());
                        if (listener != null) listener.onFailure("");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.e("firebase", downloadUrl.toString());
                        if (listener != null) listener.onSuccess(downloadUrl, finalI);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float progress = (100f * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Log.e("onProgress: ", "Upload is " + progress + "% done");
                    }
                });
            } else {
//                Todo: show dialog cannot upload too large file
                if (listener != null)
                    listener.onFailure("Failed to upload, image file is too large or corrupted");
            }
        }
    }

    public static void removeFile(String imageUrl, final OnDeleteFileListener listener) {
        initStorageInstancesIfNeeded();

        StorageReference reference = storage.getReferenceFromUrl(imageUrl);
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (listener != null) listener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("onFailure: ", "delete");
                if (listener != null) listener.onFailure();
            }
        });
    }

}
