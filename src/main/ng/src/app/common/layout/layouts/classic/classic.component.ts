import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { 
    AvatarComponent, 
    BarComponent, 
    BarRightDirective, 
    ButtonComponent, 
    ProductSwitchBodyComponent, 
    ProductSwitchComponent, 
    ProductSwitchItem, 
    ShellbarModule, 
    SideNavigationModel, 
    SideNavigationModule,
    MenuModule,
    ThemingModule
} from '@fundamental-ngx/core';
import { LayoutGridModule } from '@fundamental-ngx/core';
import { SideNavigationService } from './side-navigation.service';
import { 
    UserMenuBodyComponent, 
    UserMenuComponent, 
    UserMenuControlComponent, 
    UserMenuFooterComponent, 
    UserMenuHeaderContainerDirective, 
    UserMenuHeaderDirective, 
    UserMenuSublineDirective} from '@fundamental-ngx/core/user-menu';
import { CompleteThemeDefinition, ThemingService } from '@fundamental-ngx/core/theming';
import { AuthService } from '../../../../modules/auth/auth.service';

@Component({
    selector: 'app-classic-layout',
    standalone: true,
    templateUrl: './classic.component.html',
    // styleUrl: './classic.component.scss',
    imports: [
        ButtonComponent,
        RouterOutlet, 
        // ClickedDirective,
        // RouterLink,
        ShellbarModule,
        ProductSwitchComponent,
        ProductSwitchBodyComponent,
        UserMenuComponent,
        UserMenuControlComponent,
        UserMenuBodyComponent,
        UserMenuHeaderContainerDirective,
        UserMenuHeaderDirective,
        UserMenuFooterComponent,
        MenuModule,
        // UserMenuListComponent,
        // UserMenuSublistComponent,
        // UserMenuListItemComponent,
        UserMenuSublineDirective,
        AvatarComponent,
        // PanelComponent,
        // ListComponent,
        // ListItemComponent,
        // UserMenuBodyComponent,
        CommonModule,
        LayoutGridModule,
        // NgTemplateOutlet,
        SideNavigationModule,
        BarComponent,
        BarRightDirective,
        ThemingModule
    ] 
})
export class ClassicLayoutComponent implements OnInit {

    @ViewChild(UserMenuComponent)
    userMenuComponent!: UserMenuComponent;

    expanded: boolean = true;

    productSwitcher: ProductSwitchItem[];
    sideNavigationConfiguration: SideNavigationModel;
    
    sideNavigationExpanded: boolean = window.innerWidth > 768;

    themes: CompleteThemeDefinition[];

    /**
     * Constructor
     */
    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private sideNavigationService: SideNavigationService,
        private authService: AuthService,
        public themingService: ThemingService)
    {
        this.productSwitcher = this.sideNavigationService.productSwitcher;
        this.sideNavigationConfiguration = this.sideNavigationService.sideNavigationConfiguration;
        this.themes = this.themingService.getThemes();

    }
    
    ngOnInit(): void {
    }

    toggleSideNavigation(): void {
        this.sideNavigationExpanded = !this.sideNavigationExpanded;
    }

    isOpenChange(event: boolean): void {
        console.log('isOpenChange', event);
    }


    switchTheme(theme: string): void {
        console.log('switchTheme', theme);
        this.themingService.setTheme(theme);
        // this.router.navigate([], { queryParams: { theme: theme } });
    }

    /**
     * Sign out the user
     */
    signOut(): void {
        this.authService.logout();
    }
}
