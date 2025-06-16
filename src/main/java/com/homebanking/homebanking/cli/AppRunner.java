package com.homebanking.homebanking.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("cli")
public class AppRunner implements CommandLineRunner {
    private final InterfazServiceCli interfazCli;

    public AppRunner(InterfazServiceCli interfazCli) {
        this.interfazCli = interfazCli;
    }

    @Override
    public void run(String... args) throws Exception {
        interfazCli.iniciar();
    }
}