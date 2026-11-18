import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { FDB_NAVIGATION, FdbNavigationState } from '@fundamental-ngx/btp/navigation';
import { 
    AvatarComponent, 
    BarComponent, 
    BarRightDirective, 
    ButtonComponent, 
    ProductSwitchBodyComponent, 
    ProductSwitchComponent, 
    ProductSwitchItem, 
    ShellbarModule, 
    MenuModule,
    ThemingModule
} from '@fundamental-ngx/core';
import { LayoutGridModule } from '@fundamental-ngx/core';
import { SideNavigationItem, SideNavigationService } from './side-navigation.service';
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
    styleUrl: './classic.component.scss',
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
        FDB_NAVIGATION,
        RouterLink,
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
    navigationItems: SideNavigationItem[];

    /** BTP navigation is expanded or snapped. Selection itself follows the current route. */
    navigationState: FdbNavigationState = 'expanded';

    sideNavigationExpanded: boolean = window.innerWidth > 768;

    themes: CompleteThemeDefinition[];

    /**
     * Constructor
     */
    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private sideNavigationService: SideNavigationService,
        public authService: AuthService,
        public themingService: ThemingService,
    )
    {
        this.productSwitcher = this.sideNavigationService.productSwitcher();
        this.navigationItems = this.sideNavigationService.navigationItems();
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
