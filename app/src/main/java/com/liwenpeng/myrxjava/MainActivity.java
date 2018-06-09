package com.liwenpeng.myrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.liwenpeng.myrxjava.Api.LoginRequest;
import com.liwenpeng.myrxjava.Api.LoginResponse;
import com.liwenpeng.myrxjava.Api.MyApi;
import com.liwenpeng.myrxjava.Api.RegisterRequest;
import com.liwenpeng.myrxjava.Api.RegisterResponse;
import com.liwenpeng.myrxjava.Api.UserBaseInfoRequest;
import com.liwenpeng.myrxjava.Api.UserBaseInfoResponse;
import com.liwenpeng.myrxjava.Api.UserExtraInfoRequest;
import com.liwenpeng.myrxjava.Api.UserExtraInfoResponse;
import com.liwenpeng.myrxjava.Api.UserInfo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.BackpressureKind;
import io.reactivex.annotations.BackpressureSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private final String LOGTAG = "lwp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //第一章
       // Chapter_01();

        //第二章
       // Chapter_02();

        //第三章
       // Chapter_03();

        //第四章
     //   Chapter_04();

        //第五章:zip的结合流速不均衡的原因
        //Chapter_05();

        //第六章
       // Chapter_06();

        //第七章 :flowable
       // Chapter_07();

        //第八章
     //   Chapter_08();

        //第九章
        Chapter_09();
    }

    private void Chapter_09() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(LOGTAG,"发送 1 onNext");
                emitter.onNext(1);
                Log.d(LOGTAG,"发送 2 onNext");
                emitter.onNext(2);
                Log.d(LOGTAG,"发送 3 onNext");
                emitter.onNext(3);
                Log.d(LOGTAG,"发送  onComplete");
                emitter.onComplete();
            }
        },BackpressureStrategy.ERROR).subscribe(new FlowableSubscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(LOGTAG,"onSubscribe");
                s.request(2);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(LOGTAG,"收到onNext:"+integer);

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG,"收到onComplete");
            }
        });


    }

    private void Chapter_08() {

    }

    /**
     *
     * */
    private void Chapter_07() {
    DoRXjava_07_01();
   // DoRXjava_07_02();
    }

    private void DoRXjava_07_02() {
        Flowable.create(new FlowableOnSubscribe<Integer>()
        { @Override public void subscribe(FlowableEmitter<Integer> emitter) throws Exception { Log.d(LOGTAG, "emit 1"); emitter.onNext(1);
        Log.d(LOGTAG, "emit 2"); emitter.onNext(2); Log.d(LOGTAG, "emit 3"); emitter.onNext(3); Log.d(LOGTAG, "emit complete");
        emitter.onComplete(); } }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() { @Override public void onSubscribe(Subscription s)
        { Log.d(LOGTAG, "onSubscribe"); } @Override public void onNext(Integer integer) { Log.d(LOGTAG, "onNext: " + integer); }
        @Override public void onError(Throwable t) { Log.w(LOGTAG, "onError: ", t); } @Override public void onComplete() { Log.d(LOGTAG, "onComplete"); } });



    }

    //flowable的简单使用
    private void DoRXjava_07_01() {
        //被观察者
        Flowable<Integer> integerFlowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(LOGTAG,"emitter 1");
                emitter.onNext(1);
                Log.d(LOGTAG,"emitter 2");
                emitter.onNext(2);
                Log.d(LOGTAG,"emitter onComplete");
                emitter.onComplete();
            }
        },BackpressureStrategy.ERROR);

        //观察者
        Subscriber<Integer> integerSubscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(2); //注意这行代码
                Log.d(LOGTAG,"onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(LOGTAG, "integer:" + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(LOGTAG,"onError"+t.toString());
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG,"onComplete");
            }
        };

        integerFlowable.subscribe(integerSubscriber);


    }

    /**
     * 增加了一个filter, 只允许能被10整除的事件通过
     * sample操作符, 简单做个介绍, 这个操作符每隔指定的时间就从上游中取出一个事件发送给下游. 这里我们让它每隔2秒取一个事件给下游
     * 前面这两种方法 DoRXjava_06_02 和 DoRXjava_06_03 归根到底其实就是减少放进水缸的事件的数量, 是以数量取胜, 但是这个方法有个缺点, 就是丢失了大部分的事件.
     * 那么我们换一个角度来思考, 既然上游发送事件的速度太快, 那我们就适当减慢发送事件的速度,DoRXjava_06_04  从速度上取胜
     * */
    private void Chapter_06() {
       // DoRXjava_06_01();//爆了内存溢出
       // DoRXjava_06_02();//filter操作符
       // DoRXjava_06_03();//sample操作符
        DoRXjava_06_04();//从速度取胜 Thread.sleep

    }

    private void DoRXjava_06_04() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0 ;;i++){
                    emitter.onNext(i);
                    Thread.sleep(2000);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(LOGTAG,"integer:"+integer);
            }
        });
    }

    private void DoRXjava_06_03() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i=0;;i++){
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io()).sample(2,TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(LOGTAG,"integer:"+integer);
            }
        });
    }

    private void DoRXjava_06_02() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i=0;;i++){
                    emitter.onNext(i);
                }
            }
        }).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 10 == 0;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(LOGTAG,"integer:"+integer);
            }
        });
    }

    private void DoRXjava_06_01() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i=0;;i++){
                    emitter.onNext(i);
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Thread.sleep(2000);//这样会崩溃
                Log.d(LOGTAG,"integer:"+integer);
            }
        });
    }

    //Backpressure背压
    private void Chapter_05() {

    }

    //zip操作符
    private void Chapter_04() {
        DoRXjava_04_01();
        DoRXjava_04_02();
    }

    /**
     * 学习了Zip的基本用法, 那么它在Android有什么用呢, 其实很多场景都可以用到Zip. 举个例子.
     比如一个界面需要展示用户的一些信息, 而这些信息分别要从两个服务器接口中获取, 而只有当两个都获取到了之后才能进行展示, 这个时候就可以用Zip了
     * */
    private void DoRXjava_04_02() {
        MyApi api = createRetrofit().create(MyApi.class);
        Observable<UserBaseInfoResponse> userBaseInfoResponseObservable =
                api.getUserBaseInfo(new UserBaseInfoRequest()).subscribeOn(Schedulers.io());
        Observable<UserExtraInfoResponse> userExtraInfoResponseObservable =
                api.getUserExtraInfo(new UserExtraInfoRequest()).subscribeOn(Schedulers.io());
        Observable.zip(userBaseInfoResponseObservable, userExtraInfoResponseObservable, new BiFunction<UserBaseInfoResponse, UserExtraInfoResponse, UserInfo>() {
            @Override
            public UserInfo apply(UserBaseInfoResponse userBaseInfoResponse, UserExtraInfoResponse userExtraInfoResponse) throws Exception {
                UserInfo userInfo = new UserInfo(userBaseInfoResponse, userExtraInfoResponse);
                return userInfo;
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo userInfo) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    private void DoRXjava_04_01() {
        Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(LOGTAG,"integerObservable thread:"+Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

        Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(LOGTAG,"stringObservable thread:"+Thread.currentThread().getName());
                emitter.onNext("li");
                emitter.onNext("wenpeng");
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

        Observable.zip(integerObservable, stringObservable, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                Log.d(LOGTAG,"zip thread:"+Thread.currentThread().getName());
                s=integer+s;
                return s;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                    Log.d(LOGTAG,"onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(LOGTAG,"onNext thread:"+Thread.currentThread().getName());
                Log.d(LOGTAG,"onNext :"+s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void Chapter_03() {
       // DoRXjava_03_01();
      //  DoRXjava_03_02();
     //   DoRXjava_03_03();//操作符map和flatmap
      //  DoRXjava_03_04();//操作符map和flatmap实现自动登录注册
    }

    private void DoRXjava_03_04() {
        final MyApi api = createRetrofit().create(MyApi.class);
        boolean first = true;
        if (first)
        api.register(new RegisterRequest()).subscribeOn(Schedulers.newThread()).doOnNext(new Consumer<RegisterResponse>() {
            @Override
            public void accept(RegisterResponse registerResponse) throws Exception {
                Log.d(LOGTAG,"accept thread:"+Thread.currentThread().getName());
            }
        }).flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
            @Override
            public ObservableSource<LoginResponse> apply(RegisterResponse registerResponse) throws Exception {
                Log.d(LOGTAG,"flatMap thread:"+Thread.currentThread().getName());
                return  api.login(new LoginRequest());
            }
        }).observeOn(Schedulers.io()).subscribe(new Consumer<LoginResponse>() {
            @Override
            public void accept(LoginResponse loginResponse) throws Exception {
                Log.d(LOGTAG,"accept thread:"+Thread.currentThread().getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(LOGTAG,"accept thread:"+Thread.currentThread().getName());
            }
        });
    }

    //flatmap
    /**
     * FlatMap将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们发射的事件合并后放进一个单独的Observable里.
     * */
    private void DoRXjava_03_03() {
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);

                emitter.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                ArrayList<String> list = new ArrayList<>();
                for (int i=6;i<9;i++){
                    list.add(""+i);
                }
                return Observable.fromIterable(list).delay(1,TimeUnit.SECONDS);
            }
        }).observeOn(Schedulers.io()).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(LOGTAG,"doOnNext:"+s+"current thread:"+Thread.currentThread().getName());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(LOGTAG,"s:"+s+"current thread:"+Thread.currentThread().getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });

    }

    //map操作符
    /**
     * map是RxJava中最简单的一个变换操作符了, 它的作用就是对上游发送的每一个事件应用一个函数, 使得每一个事件都按照指定的函数去变化.
     * */
    private void DoRXjava_03_02() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.newThread()).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                Log.d(LOGTAG,"此时是map int:"+integer);
                return ""+integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(LOGTAG,"此时是String:"+s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(LOGTAG,"异常："+throwable.toString());
            }
        });

    }

    //不用操作符，新用户先注册再自动登录
    private void DoRXjava_03_01() {
//        MyRegister(api);

    }




    private void Chapter_01() {
        DoRXjava_01_01();
        DoRXjava_01_02();
        DoRXjava_01_03();
    }

    private void Chapter_02() {
        DoRXjava_02_01();
        DoRXjava_02_02();
        DoRXjava_02_03();
    }
    public Retrofit createRetrofit(){
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = client.newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.douban.com/").client(builder.build()).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        return retrofit;
    }
    public void MyRegister(final MyApi api){
        api.register(new RegisterRequest()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<RegisterResponse>() {
            @Override
            public void accept(RegisterResponse registerResponse) throws Exception {
                    Log.d("lwp","注册成功");
                MyLogin(api);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("lwp","注册失败");
            }
        });
    }
    private void MyLogin(MyApi api) {
        api.login(new LoginRequest()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<LoginResponse>() {
            @Override
            public void accept(LoginResponse loginResponse) throws Exception {
                Log.d("lwp","登录成功");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("lwp","登录失败");
            }
        });
    }

    //结合retrofit
    private void DoRXjava_02_03() {

        MyApi api = createRetrofit().create(MyApi.class);
        LoginRequest loginRequest = new LoginRequest();
        LoginResponse loginResponse = new LoginResponse();
        api.login(loginRequest).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<LoginResponse>() {
            @Override
            public void accept(LoginResponse loginResponse) throws Exception {

            }
        });

    }

    private void DoRXjava_02_02() {

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer s) throws Exception {
                Log.d(LOGTAG, "String :" + s);
                Log.d(LOGTAG, "观察者线程:" + Thread.currentThread().getName());
            }
        };

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io()).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(LOGTAG,"Consumer 1 当前线程是:"+Thread.currentThread().getName());
            }
        }).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(LOGTAG,"Consumer 2 当前线程是:"+Thread.currentThread().getName());
            }
        }).subscribe(consumer);

    }

    private void DoRXjava_02_01() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("li");
                emitter.onNext("wen");
                emitter.onNext("peng");
                Log.d(LOGTAG, "被观察者线程:" + Thread.currentThread().getName());
            }
        });
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(LOGTAG, "String :" + s);
                Log.d(LOGTAG, "观察者线程:" + Thread.currentThread().getName());
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);


    }


    //rxjava最基本用法
    public void DoRXjava_01_01(){
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(LOGTAG,"emitter.onNext: 1");
                emitter.onNext(1);
                Log.d(LOGTAG,"emitter.onNext: 2");
                emitter.onNext(2);
                Log.d(LOGTAG,"emitter.onNext: 3");
                emitter.onNext(3);

                emitter.onComplete();
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG,"onSubscribe : "+d);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(LOGTAG,"onNext integer:"+integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG,"onError:"+e);
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG,"onComplete");
            }
        };

        observable.subscribe(observer);

    }

    //rxjava链式连起来
    public void DoRXjava_01_02(){
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(LOGTAG,"emitter.onNext: 1");
                emitter.onNext(1);
                Log.d(LOGTAG,"emitter.onNext: 2");
                emitter.onNext(2);
                Log.d(LOGTAG,"emitter.onNext: 3");
                emitter.onNext(3);

                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(LOGTAG,"onSubscribe : "+d);
            }

            @Override
            public void onNext(Integer integer) {
                if (integer == 2){
                    //为什么切断了没生效？
                    disposable.dispose();
                }
                Log.d(LOGTAG,"onNext integer:"+integer);

            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG,"onError:"+e);
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG,"onComplete");
            }
        });


    }

    private void DoRXjava_01_03() {

        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(LOGTAG,"integer:"+integer);
            }
        });
    }
}
