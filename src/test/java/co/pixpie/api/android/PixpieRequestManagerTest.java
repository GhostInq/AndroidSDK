package co.pixpie.api.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import co.pixpie.api.android.auth.AuthInfo;
import co.pixpie.api.android.auth.AuthManager;
import co.pixpie.api.android.image.CropAlignType;
import co.pixpie.api.android.image.ImageFormat;
import co.pixpie.api.android.image.ImageTransformation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by accord on 11/2/16.
 */

@RunWith(JUnit4.class)
public class PixpieRequestManagerTest {

    private String originalRemoteImageUrl = "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg";
    private String originalLocalImageUrl = "uploaded_image.jpg";
    private PixpieRequestManager manager;

    @Before
    public void before() {

        AuthInfo authInfo = mock(AuthInfo.class);
        String cdnUrl = "http://pixpie-demo.azureedge.net/test.com.test";
        when(authInfo.getCdnUrl()).thenReturn(cdnUrl);

        AuthManager authManager = mock(AuthManager.class);
        when(authManager.isAuthenticated()).thenReturn(true);
        when(authManager.getAuthInfo()).thenReturn(authInfo);


        NetworkInfo networkInfo = mock(NetworkInfo.class);
        when(networkInfo.getType()).thenReturn(ConnectivityManager.TYPE_MOBILE);
        when(networkInfo.getSubtype()).thenReturn(TelephonyManager.NETWORK_TYPE_LTE);

        ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        Context context = mock(Context.class);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);

        manager = new PixpieRequestManager(null, authManager, context, null, false);
    }

    @Test
    public void test_success_remote_width_height_quality() {


        ImageTransformation transformation = new ImageTransformation()
                .withWidth(600)
                .withHeight(500)
                .withQuality(80);

        final String remoteImageUrl = manager.getRemoteImageUrl(originalRemoteImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/remote/webp/w_600,h_500,q_80/" +
                "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg", remoteImageUrl);
    }

    @Test
    public void test_success_remote_width_height_quality_cropAlignType() {

        ImageTransformation transformation = new ImageTransformation()
                .withWidth(600)
                .withHeight(500)
                .withQuality(80)
                .withCropAlignType(CropAlignType.BOTTOM_LEFT);

        final String remoteImageUrl = manager.getRemoteImageUrl(originalRemoteImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/remote/webp/w_600,h_500,q_80,c_bottom_left/" +
                "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg", remoteImageUrl);

    }

    @Test
    public void test_success_remote_width() {

        ImageTransformation transformation = new ImageTransformation()
                .withWidth(600);

        final String remoteImageUrl = manager.getRemoteImageUrl(originalRemoteImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/remote/webp/w_600,q_80/" +
                "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg", remoteImageUrl);


    }

    @Test
    public void test_success_remote_width_imageFormat() {

        ImageTransformation transformation = new ImageTransformation()
                .withWidth(600)
                .withFormat(ImageFormat.DEFAULT);

        final String remoteImageUrl = manager.getRemoteImageUrl(originalRemoteImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/remote/def/w_600,q_80/" +
                "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg", remoteImageUrl);


    }

    @Test
    public void test_success_remote_width_webp() {
        ImageTransformation transformation = new ImageTransformation()
                .withHeight(500)
                .withFormat(ImageFormat.WEBP);

        final String remoteImageUrl = manager.getRemoteImageUrl(originalRemoteImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/remote/webp/h_500,q_80/" +
                "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg", remoteImageUrl);
    }

    @Test
    public void test_success_remote_height_width_default_format_quality() {
        ImageTransformation transformation = new ImageTransformation()
                .withHeight(500)
                .withWidth(600)
                .withFormat(ImageFormat.DEFAULT)
                .withQuality(100);

        final String remoteImageUrl = manager.getRemoteImageUrl(originalRemoteImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/remote/def/w_600,h_500,q_100/" +
                "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg", remoteImageUrl);
    }

    @Test
    public void test_success_remote_width_default_format_quality() {
        ImageTransformation transformation = new ImageTransformation()
                .withWidth(600)
                .withFormat(ImageFormat.DEFAULT)
                .withQuality(100);

        final String remoteImageUrl = manager.getRemoteImageUrl(originalRemoteImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/remote/def/w_600,q_100/" +
                "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg", remoteImageUrl);
    }

    @Test
    public void test_success_remote_height_default_format_quality() {
        ImageTransformation transformation = new ImageTransformation()
                .withHeight(500)
                .withFormat(ImageFormat.DEFAULT)
                .withQuality(100);

        final String remoteImageUrl = manager.getRemoteImageUrl(originalRemoteImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/remote/def/h_500,q_100/" +
                "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg", remoteImageUrl);
    }

    @Test
    public void test_success_local_width_height() {
        ImageTransformation transformation = new ImageTransformation()
                .withHeight(500)
                .withWidth(600);

        final String localImageUrl = manager.getImageUrl(originalLocalImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/local/webp/w_600,h_500,q_80/uploaded_image.jpg",
                localImageUrl);
    }

    @Test
    public void test_success_local_width() {
        ImageTransformation transformation = new ImageTransformation()
                .withWidth(600);

        final String localImageUrl = manager.getImageUrl(originalLocalImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/local/webp/w_600,q_80/uploaded_image.jpg",
                localImageUrl);
    }

    @Test
    public void test_success_local_height() {
        ImageTransformation transformation = new ImageTransformation()
                .withHeight(500);

        final String localImageUrl = manager.getImageUrl(originalLocalImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/local/webp/h_500,q_80/uploaded_image.jpg",
                localImageUrl);
    }

    @Test
    public void test_success_local_width_height_default_format_quality() {
        ImageTransformation transformation = new ImageTransformation()
                .withFormat(ImageFormat.DEFAULT)
                .withHeight(500)
                .withWidth(600)
                .withQuality(80);

        final String localImageUrl = manager.getImageUrl(originalLocalImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/local/def/w_600,h_500,q_80/uploaded_image.jpg",
                localImageUrl);
    }

    @Test
    public void test_success_local_format_quality() {
        ImageTransformation transformation = new ImageTransformation()
                .withFormat(ImageFormat.DEFAULT);

        final String localImageUrl = manager.getImageUrl(originalLocalImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/local/def/q_80/uploaded_image.jpg",
                localImageUrl);
    }

    @Test
    public void test_success_remote_width_crop_top() {
        ImageTransformation transformation = new ImageTransformation()
                .withWidth(600)
                .withCropAlignType(CropAlignType.TOP);

        final String remoteImageUrl = manager.getRemoteImageUrl(originalRemoteImageUrl, transformation);

        assertEquals("http://pixpie-demo.azureedge.net/test.com.test/remote/webp/w_600,q_80,c_top/" +
                "https://pp.vk.me/c626730/v626730256/2f3d8/11jxm6YGqkw.jpg", remoteImageUrl);
    }



}
