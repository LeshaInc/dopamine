package ru.sigsegv.dopamine;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TempController {
    @GetMapping("/convert/c/f")
    double celsiusToFahrenheit(@RequestParam double celsius) {
        return celsius * 1.8 + 32.0;
    }

    @GetMapping("/convert/f/c")
    double fahrenheitToCelsius(@RequestParam double fahrenheit) {
        return (fahrenheit - 32.0) / 1.8;
    }
}