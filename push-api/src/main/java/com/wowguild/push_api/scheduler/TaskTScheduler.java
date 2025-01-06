package com.wowguild.push_api.scheduler;

import org.springframework.stereotype.Service;

@Service
public interface TaskTScheduler<T> {

    void schedule(T object);
}
