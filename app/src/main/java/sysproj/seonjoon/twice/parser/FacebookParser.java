package sysproj.seonjoon.twice.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sysproj.seonjoon.twice.entity.FacebookLinkVO;
import sysproj.seonjoon.twice.entity.FacebookPageVO;
import sysproj.seonjoon.twice.entity.FacebookPost;
import sysproj.seonjoon.twice.entity.Post;
import sysproj.seonjoon.twice.entity.PostMedia;
import sysproj.seonjoon.twice.entity.PostRFS;
import sysproj.seonjoon.twice.entity.UserProfile;
import sysproj.seonjoon.twice.staticdata.SNSTag;
import sysproj.seonjoon.twice.staticdata.UserSession;

public class FacebookParser extends SNSParser {

    private final static String TAG = "FacebookParser";

    public FacebookParser() {
    }

    @Override
    public ArrayList<Post> parseTimeline(JSONObject object) {

        if (object == null)
            return null;

        ArrayList<Post> resultList = new ArrayList<>();

        try {
            JSONArray dataArray = object.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                int snsType = SNSTag.Facebook * SNSTag.Platform;

                ArrayList<PostMedia> mediaList = null;
                FacebookLinkVO link = null;
                JSONObject jsonObject = dataArray.getJSONObject(i);

                String message = null;
                String createdTime = parseCreatedDate(jsonObject);
                String type = parseType(jsonObject);

                UserProfile userProfile = UserSession.FacebookProfile == null ? parseUserProfile(jsonObject) : UserSession.FacebookProfile;
                long id = parseIDPost(jsonObject);

                if (!jsonObject.isNull("message"))
                    message = jsonObject.getString("message");

                if (userProfile.getName() == null || userProfile.getName().isEmpty())
                    userProfile = new UserProfile.Builder("Unknown").build();

                switch (type) {
                    case "status":
                        snsType += SNSTag.Origin;
                        break;
                    case "photo":
                        snsType += SNSTag.Image;
                        mediaList = parseImageList(jsonObject);
                        break;
                    case "video":
                        snsType += SNSTag.Video;
                        mediaList = parseVideoList(jsonObject);
                        break;
                    case "link":
                        snsType += SNSTag.Link;
                        link = parseLink(jsonObject);
                }

                Post post = new FacebookPost.Builder(id, snsType, userProfile, message, createdTime, parseRFSField(jsonObject))
                        .shareLink(link)
                        .imageList(mediaList)
                        .build();

                resultList.add(post);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Data Array Checker JSON Exception");

            e.printStackTrace();
        }

        return resultList;
    }

    public ArrayList<Post> parseTimeline(JSONObject object, FacebookPageVO vo) {

        if (object == null)
            return null;

        ArrayList<Post> resultList = new ArrayList<>();

        try {
            JSONArray dataArray = object.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                int snsType = SNSTag.Facebook * SNSTag.Platform;

                ArrayList<PostMedia> mediaList = null;
                FacebookLinkVO link = null;
                JSONObject jsonObject = dataArray.getJSONObject(i);

                String message = null;
                String createdTime = parseCreatedDate(jsonObject);
                String type = parseType(jsonObject);

                UserProfile userProfile = new UserProfile.Builder(vo.getName()).profileImage(vo.getPageImage()).build();
                long id = parseIDPost(jsonObject);

                if (!jsonObject.isNull("message"))
                    message = jsonObject.getString("message");

                if (userProfile.getName() == null || userProfile.getName().isEmpty())
                    userProfile = new UserProfile.Builder("Unknown").build();

                switch (type) {
                    case "status":
                        snsType += SNSTag.Origin;
                        break;
                    case "photo":
                        snsType += SNSTag.Image;
                        mediaList = parseImageList(jsonObject);
                        break;
                    case "video":
                        snsType += SNSTag.Video;
                        mediaList = parseVideoList(jsonObject);
                        break;
                    case "link":
                        snsType += SNSTag.Link;
                        link = parseLink(jsonObject);
                }

                Post post = new FacebookPost.Builder(id, snsType, userProfile, message, createdTime, parseRFSField(jsonObject))
                        .shareLink(link)
                        .imageList(mediaList)
                        .build();

                resultList.add(post);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Data Array Checker JSON Exception");

            e.printStackTrace();
        }

        return resultList;

    }

    @Override
    public ArrayList<Post> parseSearch(JSONObject object) {
        if (object == null)
            return null;

        ArrayList<Post> resultList = new ArrayList<>();

        try {
            JSONArray dataArray = object.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);

                String message = null;
                String createdTime = parseCreatedDate(jsonObject);
                UserProfile userProfile = UserSession.FacebookProfile;
                long id = parseIDPost(jsonObject);

                if (!jsonObject.isNull("message"))
                    message = jsonObject.getString("message");

                if (userProfile.getName() == null || userProfile.getName().isEmpty())
                    userProfile = new UserProfile.Builder("Unknown").build();

                Post post = new FacebookPost.Builder(id, SNSTag.Facebook * SNSTag.Platform + SNSTag.Origin, userProfile, message, createdTime, new PostRFS()).build();

                resultList.add(post);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public ArrayList<FacebookPageVO> parsePageList(JSONObject object) {
        ArrayList<FacebookPageVO> returnList = new ArrayList<>();

        try {
            JSONArray dataArray = object.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject data = dataArray.getJSONObject(i);

                String token = parseToken(data);
                String name = parseName(data);
                String pageImage = parseUrl(parsePicture(data));
                long id = parseID(data);

                returnList.add(new FacebookPageVO(token, name, pageImage, id));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Missing Data - " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "page is Null");
        }

        return returnList;
    }

    @Override
    public UserProfile parseUserProfile(JSONObject jsonObject) {

        String name = "Unknown";
        String email = "Unknown@nwonknU.com";
        String profileImage = null;

        try {
            name = parseName(jsonObject);
            email = jsonObject.getString("email");
            profileImage = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
        } catch (JSONException ignored) {
        }

        return new UserProfile.Builder(name).userEmail(email).profileImage(profileImage).build();
    }

    @Override
    protected String parseCreatedDate(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("created_time");
    }

    @Override
    protected long parseID(JSONObject jsonObject) throws JSONException {
        return Long.parseLong(jsonObject.getString("id"));
    }

    private long parseIDPost(JSONObject jsonObject) throws JSONException {
        return Long.parseLong(jsonObject.getString("id").split("_")[1]);
    }

    private String parseName(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("name");
    }

    private JSONObject parseMedia(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("media");
    }

    private JSONObject parseImage(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("image");
    }

    private JSONObject parsePicture(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("picture").getJSONObject("data");
    }

    private String parseType(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("type");
    }

    private String parseToken(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("access_token");
    }

    private String parseDescription(JSONObject jsonObject) {
        try {
            return jsonObject.getString("description");
        } catch (JSONException ignored) {
        }
        return "";
    }

    private String parseTitle(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("title");
    }

    private JSONArray parseAttachmentsData(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("attachments").getJSONArray("data");
    }

    private String parseSrc(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("src");
    }

    private String parseSource(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("source");
    }

    private String parseUrl(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("url");
    }

    private ArrayList<PostMedia> parseImageList(JSONObject jsonObject) {

        ArrayList<PostMedia> resultList = new ArrayList<>();

        try {
            JSONArray data = parseAttachmentsData(jsonObject);

            for (int j = 0; j < data.length(); j++) {
                JSONObject item = data.getJSONObject(j);

                JSONObject media = parseMedia(item);
                JSONObject image = parseImage(media);

                resultList.add(new PostMedia(PostMedia.PHOTO, "", parseSrc(image)));
            }
        } catch (JSONException e) {
            resultList = null;
            e.printStackTrace();
        }

        return resultList;
    }

    private ArrayList<PostMedia> parseVideoList(JSONObject jsonObject) {
        ArrayList<PostMedia> resultList = new ArrayList<>();

        try {
            JSONArray data = parseAttachmentsData(jsonObject);

            for (int j = 0; j < data.length(); j++) {
                JSONObject item = data.getJSONObject(j);

                JSONObject media = parseMedia(item);
                JSONObject image = parseImage(media);

                resultList.add(new PostMedia(PostMedia.PHOTO, "", parseSrc(image)));
                resultList.add(new PostMedia(PostMedia.VIDEO, "", parseSource(media)));
            }
        } catch (JSONException e) {
            resultList = null;
            e.printStackTrace();
        }

        return resultList;
    }

    private FacebookLinkVO parseLink(JSONObject jsonObject) {
        FacebookLinkVO link = null;

        try {
            JSONArray data = parseAttachmentsData(jsonObject);
            JSONObject linkData = data.getJSONObject(0);

            link = new FacebookLinkVO.Builder()
                    .description(parseDescription(linkData))
                    .imageSrc(parseSrc(parseImage(parseMedia(linkData))))
                    .title(parseTitle(linkData))
                    .linkSrc(parseUrl(linkData))
                    .build();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return link;
    }

    private int parseSummaryCount(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject("summary").getInt("total_count");
    }

    private PostRFS parseRFSField(JSONObject jsonObject) {
        int like = 0;
        int comment = 0;

        try {
            JSONObject likeObject = jsonObject.getJSONObject("likes");
            like = parseSummaryCount(likeObject);
        } catch (JSONException ignored) {
        }

        try {
            JSONObject commentObject = jsonObject.getJSONObject("comments");
            comment = parseSummaryCount(commentObject);
        } catch (JSONException ignored) {
        }

        return new PostRFS(comment, like, 0);
    }
}
