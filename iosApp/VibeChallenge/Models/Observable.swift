//
//  Observable.swift
//  VibeChallenge
//
//  Created by Thien Nguyen Duc on 06/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation

class Observable<T> {
    private(set) var value: T? {
        didSet {
            callback?(value)
        }
    }
    private var callback: ((T?) -> Void)?
    
    init(value: T? = nil) {
        self.value = value
    }
    
    func updateValue(_ value: T?) {
        self.value = value
    }
    
    func observe(callback: @escaping (T?) -> Void) {
        self.callback = callback
    }
}
