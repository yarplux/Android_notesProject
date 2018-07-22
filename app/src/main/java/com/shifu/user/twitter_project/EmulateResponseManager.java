/** Copyright 2015 Eugene Matsyuk (matzuk2@mail.ru)
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

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class EmulateResponseManager {

    private static int MAX_LIMIT;
    private static String[] source;

    private static final long FAKE_RESPONSE_TIME_IN_MS = 200;

    private static volatile EmulateResponseManager client;

    public static EmulateResponseManager getInstance(Context context) {

        MAX_LIMIT = context.getResources().getStringArray(R.array.dataSource).length;
        source = context.getResources().getStringArray(R.array.dataSource);

        if (client == null) {
            synchronized (EmulateResponseManager.class) {
                if (client == null) {
                    client = new EmulateResponseManager();
                }
            }
        }
        return client;
    }

    public Observable<List<Item>> getEmulateResponse(int offset, int limit) {
        return Observable
                .defer(() -> Observable.just(getFakeItemList(offset, limit)))
                .delaySubscription(FAKE_RESPONSE_TIME_IN_MS, TimeUnit.MILLISECONDS);
    }

    private List<Item> getFakeItemList(int offset, int limit) {
        List<Item> list = new ArrayList<>();

        // If offset > MAX_LIMIT then there is no Items in Fake server. So we return empty List
        if (offset > MAX_LIMIT) {
            return list;
        }

        int concreteLimit = offset + limit;

        // In Fake server there are only MAX_LIMIT Items.
        if (concreteLimit > MAX_LIMIT) {
            concreteLimit = MAX_LIMIT;
        }

        for (int i = offset; i < concreteLimit; i++) {
            list.add(new Item(i, source[i]));
        }
        return list;
    }

}