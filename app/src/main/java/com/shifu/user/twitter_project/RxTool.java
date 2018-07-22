/**
 * Copyright 2015 Eugene Matsyuk (matzuk2@mail.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.shifu.user.twitter_project;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class RxTool<T> {

    private RecyclerView recyclerView;
    private myListener<T> myListener;
    private int limit;

    RxTool(RecyclerView recyclerView, myListener<T> myListener, int limit) {
        this.recyclerView = recyclerView;
        this.myListener = myListener;
        this.limit = limit;
    }

    public Observable<T> getPagingObservable() {
        return getScrollObservable(recyclerView, limit)
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .switchMap(offset -> getPagingObservable(myListener, myListener.onNextPage(offset), offset));
    }

    private Observable<Integer> getScrollObservable(RecyclerView recyclerView, int limit) {
        //TODO change to new method - change general logic
        return Observable.unsafeCreate(subscriber -> {
            final RecyclerView.OnScrollListener sl = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (!subscriber.isUnsubscribed()) {
                        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        int updatePosition = recyclerView.getAdapter().getItemCount() - 1 - (limit / 2);
                        if (position >= updatePosition) {
                            int offset = recyclerView.getAdapter().getItemCount();
                            subscriber.onNext(offset);
                        }
                    }
                }
            };
            recyclerView.addOnScrollListener(sl);
            subscriber.add(Subscriptions.create(() -> recyclerView.removeOnScrollListener(sl)));
            if (recyclerView.getAdapter().getItemCount() == 0) {
                int offset = 0;
                subscriber.onNext(offset);
            }
        });
    }

    private Observable<T> getPagingObservable(myListener<T> listener, Observable<T> observable, int offset) {
        return observable.onErrorResumeNext(throwable -> getPagingObservable(listener, listener.onNextPage(offset), offset));
    }

}
