package com.example.rxjavaex;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.rxjavaex.databinding.ActivityMainBinding;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createObservable();

        //coldObservable();
        //hotObservableConnect();
        //hotObservableAutoConnect();
    }

    private void createObservable(){
        Observable<String> source = Observable.create(emitter -> {
            emitter.onNext("Hello");
            emitter.onError(new Throwable());
            emitter.onNext("Hi");
            //emitter.onComplete();
        });
        source.subscribe(System.out::println,
                throwable -> System.out.println("Good bye"));
    }

    private void justObservable(){
        Observable<String> source = Observable.just("Hello","Hi");
        source.subscribe(System.out::println);
    }

    private void coldObservable(){
        Observable<Long> src = Observable.interval(1, TimeUnit.SECONDS);
        src.subscribe(value-> System.out.println("First: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        src.subscribe(value-> System.out.println("Second: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void hotObservableConnect(){
        ConnectableObservable<Long> src = Observable.interval(1,TimeUnit.SECONDS).publish();
        src.connect();
        src.subscribe(value->System.out.println("First: " +value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        src.subscribe(value-> System.out.println("Second: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void hotObservableAutoConnect(){
        System.out.println("AutoConnect Start");
        Observable<Long> src =
                Observable.interval(1,TimeUnit.SECONDS)
                        .publish()
                        .autoConnect(2);

        src.subscribe(value->System.out.println("First: " +value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        src.subscribe(value-> System.out.println("Second: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}