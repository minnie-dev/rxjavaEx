package com.example.rxjavaex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.rxjavaex.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.observers.DisposableMaybeObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /*try {
            fromArrayObservable();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        subscribeOnObserveOn();

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

    private void fromArrayObservable() throws InterruptedException {
        String[] itemArray = new String[]{"Morning", "Afternoon", "Evening"};
        Observable.fromArray(itemArray)
                .observeOn(Schedulers.computation())
                .subscribe(data->{
                    System.out.println("Observe On : " +Thread.currentThread().getName()+" | "+"value : "+data);
                });
        Thread.sleep(100);
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

    private void rangeObservable(){
        Observable.range(10,3)
                .subscribe(System.out::println);
    }


    private void singleObservable() {
        //Single.create(emitter -> emitter.onSuccess("Hello")).subscribe(System.out::println);
        // Single.just()
        Single.just("Single Test - Single.just()")
                .subscribe(System.out::println);

        // Single.create()
        Single.create(emitter -> emitter.onSuccess("Single Test - Single.create()"))
            .subscribe(System.out::println);
    }

    private void maybeObservable(){
        Maybe.empty()
                .subscribeWith(new DisposableMaybeObserver<Object>() {
                    @Override
                    public void onSuccess(@NonNull Object o) {
                        System.out.println("onSuccess");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete : Null");
                    }
                });
    }

    private void completableObservable(){
        Completable.create(CompletableEmitter::onComplete).subscribe(()->System.out.println("Completable Test >>> completed 1"));
    }

    private void intervalObservable(){
        Observable.interval(1000L, TimeUnit.MILLISECONDS)
                .map(data -> (data + 1) * 100)
                .take(5)
                .subscribe(System.out::println);
    }

    private void timerObservable(){
        Observable.timer(10,TimeUnit.SECONDS)
                .map(data -> (data + 1) * 100)
                .take(5)
                .subscribe(System.out::println);
    }

    private void rangeObservable1() {
        Observable.range(1, 10)
                //.filter(num -> num % 2 == 0)
                .subscribe(it -> System.out.println(it));

    }

    private void deferObservable(){
        String timeFormat = (new SimpleDateFormat("mm:ss.SSS", Locale.KOREA).format(System.currentTimeMillis()));

        System.out.println("[1] : "+timeFormat);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[2] : "+timeFormat);
        System.out.println("===================================");

        Observable.just(timeFormat)
                .subscribe(System.out::println);

        Observable.defer(()->Observable.just(timeFormat))
                .subscribe(System.out::println);

    }

    private void repeatObservable(){
        Observable.fromArray("1","2","3")
                .repeat(3)
                .subscribe(System.out::println);

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
        source.subscribe(data -> System.out.println("take(3) value = "+data));

        source = Observable.fromArray(nums).takeLast(3);
        source.subscribe(data -> System.out.println("takeLast(3) value = "+data));

        source = Observable.fromArray(nums).skip(2);
        source.subscribe(data -> System.out.println("skip(2) value = "+data));

        source = Observable.fromArray(nums).skipLast(2);
        source.subscribe(data -> System.out.println("skipLast(2) value = "+data));
    }

    private void zipRxJava(){
        Observable<Integer> source = Observable.zip(
                Observable.just(100, 200, 300),
                Observable.just(10, 20, 30),
                Observable.just(1, 2, 3),
                (a, b, c) -> a + b + c );
        source.subscribe(System.out::println);



        Observable.just(100, 200, 300).zipWith(
                Observable.just(10, 20, 30),
                (a, b) -> a + b
        ).subscribe(System.out::println);
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

    public void imperativeProgramming() {
        ArrayList<Integer> items = new ArrayList<>();
        items.add(1);
        items.add(2);
        items.add(3);
        items.add(4);

        for (Integer item : items) {
            if (item % 2 == 0) {
                System.out.println(item);
            }
        }

        items.add(5);
        items.add(6);
        items.add(7);
        items.add(8);
    }

    public void reactiveProgramming() {
        PublishSubject<Integer> items = PublishSubject.create();
        items.onNext(1);
        items.onNext(2);
        items.onNext(3);
        items.onNext(4);

        items.filter(item -> item % 2 == 0)
                .subscribe(System.out::println);

        items.onNext(5);
        items.onNext(6);
        items.onNext(7);
        items.onNext(8);
    }


    private void subscribeOnObserveOn(){
        ArrayList<MyShape> shapes = new ArrayList<>();
        shapes.add(new MyShape("Red","Ball"));
        shapes.add(new MyShape("Green","Ball"));
        shapes.add(new MyShape("Blue","Ball"));

        Observable.fromIterable(shapes)
                .subscribeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(data -> MyUtil.printData("doOnSubscribe"))
                .doOnNext(data -> MyUtil.printData("doOnNext", data))
                .observeOn(Schedulers.newThread())                  // (C)
                .map(data -> {data.shape = "Square"; return data;})
                .doOnNext(data -> MyUtil.printData("map(Square)", data))
                .observeOn(Schedulers.newThread())                  // (D)
                .map(data -> {data.shape = "Triangle"; return data;})
                .doOnNext(data -> MyUtil.printData("map(Triangle)", data))
                .observeOn(Schedulers.newThread())                  // (E)
                .subscribe(data -> MyUtil.printData("subscribe", data));
    }

    class MyShape{
        String color;
        String shape;

        MyShape(String color,String shape){
            this.color = color;
            this.shape = shape;
        }

        @Override
        public String toString() {
            return "MyShape{" +
                    "color='" + color + '\'' +
                    ", shape='" + shape + '\'' +
                    '}';
        }
    }

    static class MyUtil{
        static void printData(String message) {
            System.out.println(""+Thread.currentThread().getName()+" | "+message+" | ");
        }

        static void printData(String message, Object obj) {
            System.out.println(""+Thread.currentThread().getName()+" | "+message+" | " +obj.toString());
        }
    }
}