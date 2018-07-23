package com.wujiuye.qqgroupjoin.rootcmd;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.stericson.RootShell.*;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;

public class Vevent {


    /**
     * 使用RootShell工具执行shell指令
     * @param command
     * @throws InterruptedException
     * @throws IOException
     * @throws TimeoutException
     * @throws RootDeniedException
     */
    public static void executeCommand(String command) throws InterruptedException, IOException, TimeoutException, RootDeniedException {
        Command cmd = new Command(0, command);
        RootShell.getShell(true).add(cmd);
    }

    /**
     * 执行模拟事件的sendevent执行
     * @param event_num
     * @param param_1
     * @param param_2
     * @param param_3
     */
    public static void sendevent(String event_num, int param_1, int param_2, int param_3) {
        try {
            executeCommand(String.format("sendevent /dev/input/%s %d %d %d", event_num, param_1, param_2, param_3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>root权限和envent文件读写权限<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public interface OnRootShellExecuteCmdResultListener {
        //获取root权限和envent文件读写权限
        void onGetRootAndEnevtRWFinish(boolean statu);
    }

    private OnRootShellExecuteCmdResultListener mOnRootShellExecuteCmdResultListener = null;

    public void setOnRootShellExecuteCmdResultListener(OnRootShellExecuteCmdResultListener mOnRootShellExecuteCmdResultListener) {
        this.mOnRootShellExecuteCmdResultListener = mOnRootShellExecuteCmdResultListener;
    }

    public void getRootAndEnevtRW() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean scuess = executeGetRootAndEnevtRW();
                Message msg = new Message();
                msg.what = 0x001;
                msg.obj = scuess;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x001) {
                if (mOnRootShellExecuteCmdResultListener != null) {
                    mOnRootShellExecuteCmdResultListener.onGetRootAndEnevtRWFinish((boolean)msg.obj);
                }
            }
        }
    };

    /**
     * 获取root权限成功之后返回true,失败返回false
     * @return
     */
    private boolean executeGetRootAndEnevtRW() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("chmod 666 /dev/input\n");
            os.writeBytes("exit\n");//执行完指令记得exit，否则process.waitFor()会一直处于等待状态
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>执行shell指令并获取输出结果<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static String execCommand(String command) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        //这句话就是shell与高级语言间的调用
        Process proc = runtime.exec(command);
        //如果有参数的话可以用另外一个被重载的exec方法
        //实际上这样执行时启动了一个子进程,它没有父进程的控制台
        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
        InputStream inputstream = proc.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

        String line = "";
        StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        //使用exec执行不会等执行成功以后才返回,它会立即返回
        //所以在某些情况下是很要命的(比如复制文件的时候)
        //使用wairFor()可以等待命令执行完成以后才返回
        try {
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
        }
        catch (InterruptedException e) {
            System.err.println(e);
        }
        return line;
    }


}