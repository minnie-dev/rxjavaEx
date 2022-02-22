package com.example.rxjavaex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.rxjavaex.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observables.ConnectableObservable;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        zipRxJava();
    }

    private void createObservable() {
        Observable<String> source = Observable.create(emitter -> {
            emitter.onNext("Hello");
            emitter.onError(new Throwable());
            emitter.onNext("Hi");
            //emitter.onComplete();
        });
        source.subscribe(System.out::println,
                throwable -> System.out.println("Good bye"));
    }

    private void justObservable() {
        Observable<String> source = Observable.just("Hello", "Hi");
        source.subscribe(System.out::println);
    }

    private void fromArrayObservable() {
        String[] itemArray = new String[]{"Morning", "Afternoon", "Evening"};
        Observable<String> source = Observable.fromArray(itemArray);
        source.subscribe(System.out::println);
    }

    private void fromIterableObservable() {
        ArrayList<String> itemList = new ArrayList<String>();
        itemList.add("Morning");
        itemList.add("Afternoon");
        itemList.add("Evening");
        Observable<String> source = Observable.fromIterable(itemList);
        source.subscribe(System.out::println);
    }

    private void fromFutureObservable() {
        Future<String> future = Executors.newSingleThreadExecutor()
                .submit(() -> {
                    Thread.sleep(5000);
                    return "This is the future";
                });
        Observable<String> source = Observable.fromFuture(future);
        source.subscribe(System.out::println); //블로킹되어 기다림
    }

    private void singleObservable() {
        Single.create(emitter -> emitter.onSuccess("Hello")).subscribe(System.out::println);
    }

    private void mapRxJava(){
        String[] numArr = new String[]{"1","2","3"};
        Observable.fromArray(numArr)
                .map(Integer::parseInt)
                .subscribe(System.out::println);
    }

    private void flatMapRxJava(){
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .take(3)
                .flatMap(data -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .take(2)
                        .map(val -> "data: " + data + " value: " + val))
                .subscribe(System.out::println);
    }

    private void concatMapRxJava(){
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .take(3)
                .concatMap(data -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .take(2)
                        .map(val -> "data: " + data + " value: " + val))
                .subscribe(System.out::println);
    }

    private void switchMapRxJava(){
        Observable.interval(100L, TimeUnit.MILLISECONDS)
                .take(3)
                .switchMap(data -> Observable.interval(200L, TimeUnit.MILLISECONDS)
                        .take(2)
                        .map(val -> "data: " + data + " value: " + val))
                .subscribe(System.out::println);
    }

    private void reduceRxJava(){
        String[] balls = new String[] {"A", "B", "C"};

        Observable.fromArray(balls)
                .reduce((ball1, ball2) -> ball2 + "(" + ball1 + ")")
                .subscribe(System.out::println);
    }

    private void scanRxJava(){
        String[] balls = new String[] {"A", "B", "C"};
        Observable.fromArray(balls)
                .scan((ball1, ball2)-> ball2 + "("+ball1+")")
                .subscribe(System.out::println);
    }

    private void filterRxJava(){
        Integer[] nums = {10,15,76,38,29};

        Observable.fromArray(nums)
                .filter(num->num%2==0)
                .subscribe(System.out::println);
    }

    private void otherFilter(){
        Integer[] nums= {100,200,300,400,500};
        Single<Integer> single;
        Observable<Integer> source;

        single = Observable.fromArray(nums).first(-1);
        single.subscribe(data -> System.out.println("first() value = "+data));

        single = Observable.fromArray(nums).last(999);
        single.subscribe(data -> System.out.println("last() value = "+data));

        source = Observable.fromArray(nums).take(3);
        source.subscribe(data -> System.out.println("take() value = "+data));

        source = Observable.fromArray(nums).takeLast(3);
        source.subscribe(data -> System.out.println("takeLast() value = "+data));

        source = Observable.fromArray(nums).skip(2);
        source.subscribe(data -> System.out.println("skip() value = "+data));

        source = Observable.fromArray(nums).skipLast(2);
        source.subscribe(data -> System.out.println("skipLast() value = "+data));

    }

    private void zipRxJava(){
        Observable<Integer> source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(10, 20, 30),
                Observable.just(1, 2, 3),
                (a, b, c) -> a + b + c );
        source.subscribe(System.out::println);
    }


    private void coldObservable() {
        Observable<Long> src = Observable.interval(1, TimeUnit.SECONDS);
        src.subscribe(value -> System.out.println("First: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        src.subscribe(value -> System.out.println("Second: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void hotObservableConnect() {
        ConnectableObservable<Long> src = Observable.interval(1, TimeUnit.SECONDS).publish();
        src.connect();
        src.subscribe(value -> System.out.println("First: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        src.subscribe(value -> System.out.println("Second: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void hotObservableAutoConnect() {
        System.out.println("AutoConnect Start");
        Observable<Long> src =
                Observable.interval(1, TimeUnit.SECONDS)
                        .publish()
                        .autoConnect(2);

        src.subscribe(value -> System.out.println("First: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        src.subscribe(value -> System.out.println("Second: " + value));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}