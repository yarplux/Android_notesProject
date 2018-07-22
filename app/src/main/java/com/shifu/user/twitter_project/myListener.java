package com.shifu.user.twitter_project;


import rx.Observable;

public interface myListener<T> {
    Observable<T> onNextPage(int offset);
}
