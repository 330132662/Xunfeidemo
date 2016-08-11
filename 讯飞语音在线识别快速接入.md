# 讯飞语音在线识别快速接入 #
packagename：com.czh.xfdemo  
1 <a target="_blank" href="http://www.xfyun.cn/index.php/default/index">注册登录创建应用</a> ， <a target="_blank" href="http://www.xfyun.cn/doccenter/awd">直接查看在线文档</a>或<a target="_blank" href="http://www.xfyun.cn/index.php/default/doccenter/doccenterInner?itemTitle=anN3ZA==">下载文档压缩包</a>，<a href="http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=7595" target="_blank">这个是关于语音识别重要提示，一定要看！！！</a>； <a target="_blank" href="http://www.xfyun.cn/sdk/dispatcher">下载sdk压缩包（内含api文档）</a> ，<a target="_blank" href="http://xfyun.cn/index.php/default/doccenter/doccenterInner?itemTitle=ZmFx&anchor=Y29udGl0bGU2Mw">错误码列表</a>
我遇到的错误码解决方案：1、<a href="http://blog.csdn.net/q610098308/article/details/46981715" target="_blank">21001语音组件没弄好</a>  2 、<a href="http://blog.csdn.net/q610098308/article/details/46981715" target="_blank">ERROR_UNKNOWN20999未知错误</a>
<br>2  界面xml代码（只有一个界面） <pre>
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".ui.MainActivity">


    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:onClick="reqVoice"
        android:text="语音识别" />

    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:onClick="startunder"
        android:text="语义理解" />

    <Button
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:onClick="introduce"
        android:text="介绍" />

    <!--<Button-->
        <!--android:layout_width="match_parent"-->
        <!--android:layout_height="wrap_content"-->
        <!--android:onClick="bequiet"-->
        <!--android:text="一键静音" />-->

    <!--<Button-->
        <!--android:layout_width="match_parent"-->
        <!--android:layout_height="wrap_content"-->
        <!--android:onClick="cover"-->
        <!--android:text="恢复音量" />-->

    <TextView
        android:id="@+id/temprecvoice"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="说的话显示到这里" />

    <TextView
        android:id="@+id/temprecrersult"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="返回结果显示到这里" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#000000"
        android:orientation="vertical"
        android:padding="2dp">

        <WebView
            android:id="@+id/web"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="2dp" />
    </LinearLayout>
</LinearLayout>

</pre>
 界面activity： 自行处理好其他环境  依赖什么的吧  ，急用的到我的github下这个<a href="https://github.com/330132662/Xunfeidemo.git" target="_blank">demo</a> 
<pre>
 public class MainActivity extends BaseActivity {
    private TextView temprecvoice, temprecrersult;
    private int ret;//返回码
    private RecognizerListener mRecognizerListener;
    private SpeechSynthesizer mTts;// 合成播放对象
    // 语法、词典临时变量
    private String mContent;
    private static final String GRAMMAR_TYPE_ABNF = "abnf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initReconLis();// 要一直开着
        temprecvoice = fView(R.id.temprecvoice);// 识别说的话
        temprecrersult = fView(R.id.temprecrersult);// 显示匹配的回答
    }

    /*onclick区*/
    public void reqVoice(View view) {
        receiveVoice();

    }

    //  语音播报
    public void introduce(View v) {
        say();
    }

    /**
     * 理解语义:
     *
     * @param v
     */
    public void startunder(View v) {
        under();
    }/*onclick区over*/

    /**
     * 语音识别监听  有点问题，能识别到声音  因为音量改变了
     */
    private void initReconLis() {
        mRecognizerListener = new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {
//                DebugLogUtil.getInstance().Info("onVolumeChanged=" + i);
            }

            @Override
            public void onBeginOfSpeech() {
                DebugLogUtil.getInstance().Info("语音识别onBeginOfSpeech");
            }

            @Override
            public void onEndOfSpeech() {
                DebugLogUtil.getInstance().Info("语音识别onEndOfSpeech");
            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                DebugLogUtil.getInstance().Info("语音识别onResult" + recognizerResult);
            }

            @Override
            public void onError(SpeechError speechError) {
                DebugLogUtil.getInstance().Info("语音识别onError=" + speechError.getMessage());
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
                DebugLogUtil.getInstance().Info("语音识别onEvent=");
            }
        };
    }


    /**  目前此功能还不能用  因为自定义语义文件上传不上去 正在问客服
     * 语音识别 :即语法识别，主要指基于命令词的识别，识别指定关键词组合的词汇，或者固定说法的短句。
     * 语法识别分云端识别和本地识别，云端和本地分别采用ABNF和BNF语法格式。
     */
    private void receiveVoice() {
        //构建语法监听器
        GrammarListener grammarListener = new GrammarListener() {
            @Override
            public void onBuildFinish(String grammarId, SpeechError error) {
                if (error == null) {
                    if (!TextUtils.isEmpty(grammarId)) {
//构建语法成功，请保存grammarId用于识别
                    } else {
                        DebugLogUtil.getInstance().Info("语法构建失败,错误码: " + error);

                    }
                }
            }
        };
        //云端语法识别：如需本地识别请参照本地识别
//1.创建SpeechRecognizer对象
        SpeechRecognizer recognizer = SpeechRecognizer.createRecognizer(this, null);
// ABNF语法示例，可以说”北京到上海”
        mContent = "北京|上海";
//2.构建语法文件
        recognizer.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        ret = recognizer.buildGrammar(GRAMMAR_TYPE_ABNF, mContent, grammarListener);
//3.开始识别,设置引擎类型为云端
        if (ret != ErrorCode.SUCCESS) {
            DebugLogUtil.getInstance().Info("语法构建失败,错误码: " + ret);
        } else {
            DebugLogUtil.getInstance().Info("语法构建成功" + ret);

        }
        recognizer.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
//设置grammarId
        recognizer.setParameter(SpeechConstant.CLOUD_GRAMMAR, "公司宣传");
        ret = recognizer.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            DebugLogUtil.getInstance().Info("识别失败,错误码: " + ret);
        } else {
            DebugLogUtil.getInstance().Info("识别成功: " + ret);

        }
//        DebugLogUtil.getInstance().Info("ret=" + ret);


    }


    /**
     * 语音合成： 播放文本
     */
    private void say() {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
//2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
//设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
//保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
//如果不需要保存合成音频，注释该行代码
//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

//合成监听器
        SynthesizerListener mSynListener = new SynthesizerListener() {
            //会话结束回调接口，没有错误时，error为null
            public void onCompleted(SpeechError error) {
                DebugLogUtil.getInstance().Info("没有错误时，error为null:" + error);
            }

            //缓冲进度回调
            //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
            public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
                if (percent % 20 == 0) {
                    DebugLogUtil.getInstance().Info("缓冲进度=" + percent);
                }
            }

            //开始播放
            public void onSpeakBegin() {
                DebugLogUtil.getInstance().Info("开始播放=");
            }

            //暂停播放
            public void onSpeakPaused() {
                DebugLogUtil.getInstance().Info("暂停播放=");
            }

            //播放进度回调
            //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
                if (percent % 20 == 0) {
                    DebugLogUtil.getInstance().Info("播放进度=" + percent);
                }
            }

            //恢复播放回调接口
            public void onSpeakResumed() {
                DebugLogUtil.getInstance().Info("语音合成onSpeakResumed=");
            }

            //会话事件回调接口
            public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                DebugLogUtil.getInstance().Info("语音合成onEvent=");
            }
        };
        //3.开始合成
        mTts.startSpeaking("我去下班了", mSynListener);
    }

    /**
     * 理解语义
     */
    private void under() {
        //1.创建文本语义理解对象
        SpeechUnderstander understander = SpeechUnderstander.createUnderstander(this, null);
//2.设置参数，语义场景配置请登录http://osp.voicecloud.cn/
        understander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        understander.setParameter(SpeechConstant.DOMAIN, "iat");
        understander.setParameter(SpeechConstant.NLP_VERSION, "2.0");
        understander.setParameter(SpeechConstant.RESULT_TYPE, "json");
// XmlParser为结果解析类，见SpeechDemo
        SpeechUnderstanderListener mUnderstanderListener = new SpeechUnderstanderListener() {
            public void onResult(UnderstanderResult result) {
//                TODO 解析返回值
                under();
                String resultStr = result.getResultString();
                String textAns = "";
                String textQues = "";

                try {
                    JSONObject resObj = new JSONObject(resultStr);// 将结果字符串转为json对象
                    textQues = resObj.getString("text");
                    JSONObject answerBean = resObj.getJSONObject("answer");
                    textAns = answerBean.getString("text");
                } catch (JSONException e) {
                    e.printStackTrace();
                    DebugLogUtil.getInstance().Info("解析出错：" + e.getMessage());
                }
                temprecvoice.setText(textQues);
                temprecrersult.setText(textAns);// 显示问题和答案  如果问题没有回答的话 只显示问题了
                DebugLogUtil.getInstance().Info("语义理解result=" + resultStr);
            }

            public void onError(SpeechError error) {
                DebugLogUtil.getInstance().Info("语义理解error=" + error.getMessage());
            }//会话发生错误回调接口

            @Override
            public void onVolumeChanged(int i, byte[] bytes) {
//                DebugLogUtil.getInstance().Info("语义理解onVolumeChanged" + i);
            }

            public void onBeginOfSpeech() {
//                DebugLogUtil.getInstance().Info("开始录音onBeginOfSpeech");
            }//开始录音

            public void onEndOfSpeech() {
//                TODO 这里不能再次启动录音
                DebugLogUtil.getInstance().Info("结束录音onEndOfSpeech");
            }//结束录音

            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//                DebugLogUtil.getInstance().Info("扩展用接口");
            }//扩展用接口
        };
        //3.开始语义理解
        understander.startUnderstanding(mUnderstanderListener);
    }
}

</pre>
