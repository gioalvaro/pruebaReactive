package ar.ingar.pruebaReactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;


@Service
public class TestService {
    private final String UPLOAD_ROOT = "";

    @Bean
    CommandLineRunner setUp() {
        return (args) -> {
            Flux.just("red", "blue", "yellow").log().map(String::toUpperCase).subscribe();
        };
    }

    @Bean
    CommandLineRunner setUp2() {
        return (args) -> {
            Flux.just("red", "blue", "yellow").log().map(String::toUpperCase).subscribe(new Subscriber<String>() {
                @Override
                public void onSubscribe(Subscription s) {
                    s.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(String s) {
                    System.out.println(s);
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                }

                @Override
                public void onComplete() {
                    System.out.println("Esto ha sido completado");

                }
            });
        };
    }

    @Bean
    CommandLineRunner setUp3() {
        return (args) -> {
            Flux.just("red", "blue", "yellow").log().map(String::toUpperCase).subscribe(new Subscriber<String>() {

                private long count = 0;
                private Subscription subscription;

                @Override
                public void onSubscribe(Subscription subscription) {
                    this.subscription = subscription;
                    subscription.request(2);
                }

                @Override
                public void onNext(String t) {
                    count++;
                    if (count >= 2) {
                        count = 0;
                        subscription.request(2);
                    }
                }
                @Override
                public void onError(Throwable t) {
                }
                @Override
                public void onComplete() {
                }
            });
        };
    }

    @Bean
    CommandLineRunner setUp4() {
        return (args) -> {
            Flux.just("red", "blue", "yellow").log().map(String::toUpperCase).limitRate(3).subscribe();
        };
    }

    @Bean
    CommandLineRunner setUp5() {
        return (args) -> {
            Flux.just("red", "blue", "yellow").log().map(String::toUpperCase).limitRate(3).subscribe();
        };
    }

    @Bean
    CommandLineRunner setUp6() {
        return (args) -> {
            Flux.just("red", "blue", "yellow").log().map(String::toUpperCase).subscribeOn(Schedulers.parallel()).limitRate(3).subscribe();
        };
    }
}
