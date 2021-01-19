//
//  String+Extensions.swift
//  VibeChallenge
//
//  Created by Thien Nguyen Duc on 07/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation

extension String {
    var localized: String {
        return NSLocalizedString(self, comment: "")
    }
}
