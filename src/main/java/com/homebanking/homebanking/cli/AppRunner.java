package com.homebanking.homebanking.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.homebanking.homebanking.cli.MenuCli;

@Component
@Profile("cli")
public class AppRunner implements CommandLineRunner {
    private final MenuCli menuCli;

    public AppRunner(MenuCli menuCli) {
        this.menuCli = menuCli;
    }

    @Override
    public void run(String... args) throws Exception {
        menuCli.iniciar();
    }
}