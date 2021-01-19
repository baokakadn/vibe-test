//
//  UIImageView+Extensions.swift
//  VibeChallenge
//
//  Created by Thien Nguyen Duc on 30/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import UIKit

extension UIImageView {
    func loadURLString(_ string: String?) {
        guard let string = string, let url = URL(string: string) else {
            image = nil
            return
        }
        DispatchQueue.global().async {
            do {
                let data = try Data(contentsOf: url)
                DispatchQueue.main.async { [weak self] in
                    self?.image = UIImage(data: data)
                }
            } catch {
                DispatchQueue.main.async { [weak self] in
                    self?.image = nil
                }
            }
        }
    }
}
