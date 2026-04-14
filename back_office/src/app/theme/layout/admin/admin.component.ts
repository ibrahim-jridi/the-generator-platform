import { Component, HostListener, OnInit } from '@angular/core';
import { DashboardService } from '../../../shared/services/dashboard.service';
import { BSConfig } from '../../../app-config';
import { RoleService } from '../../../shared/services/role.service';
import { BsConfigService } from '../../../shared/services/bs-config.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  public hideLogo: boolean;
  public nextConfig: any;
  public navCollapsed: boolean;
  public navCollapsedMob: boolean;
  public windowWidth: number;
  public year = new Date().getFullYear();
  public appVersion: string;
  public isDashboard: boolean = false;
  public dashboardStateSub: Subscription;

  constructor(private dashboardService: DashboardService, private roleService: RoleService, private bsConfigService: BsConfigService) {
    this.nextConfig = BSConfig.config;
    this.windowWidth = window.innerWidth;
    this.navCollapsed = this.windowWidth >= 992 ? this.nextConfig.collapseMenu : false;
    this.navCollapsedMob = false;
  }

  ngOnInit(): void {
    this.loadRoleAuthorities();
    this.initAppVersion();
    if (this.windowWidth < 992) {
      this.nextConfig.layout = 'vertical';
      setTimeout(() => {
        document.querySelector('.pcoded-navbar').classList.add('menupos-static');
        (document.querySelector('#nav-ps-next') as HTMLElement).style.maxHeight = '100%'; // 100% amit
      }, 500);
    }
    this.hideLogo = false;
    this.bsConfigService.initConfig();

    this.checkWindowSize();
    this.dashboardStateSub = this.dashboardService.dashboardStateObservable.subscribe((isDashboard) => {
      this.isDashboard = isDashboard;
    });
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: Event): void {
    this.windowWidth = window.innerWidth;
    this.checkWindowSize();
  }

  checkWindowSize(): void {
    if (this.windowWidth <= 991) {
      this.navCollapsed = true;
      this.hideLogo = true;
    }
  }

  private loadRoleAuthorities(): void {
    this.roleService.getRoleAuthorities('DPM_ADMINISTRATOR').subscribe((data) => {
      if (data && data.length > 0) {
        this.roleService.authoritiesSubject.next(data);
      }
    });
  }

  initAppVersion(): void {
    this.appVersion = '0.0.1';
    /*
    this.dashboardService.getAppVersion()
    .subscribe(data => {
      ;this.appVersion = data.version
    });
     */
  }

  navMobClick(): void {
    if (this.windowWidth < 992) {
      if (this.navCollapsedMob && !document.querySelector('app-navigation.pcoded-navbar').classList.contains('mob-open')) {
        this.navCollapsedMob = !this.navCollapsedMob;
        setTimeout(() => {
          this.navCollapsedMob = !this.navCollapsedMob;
        }, 100);
      } else {
        this.navCollapsedMob = !this.navCollapsedMob;
      }
    }
  }

  changeValueCollapsed(): void {
    this.navCollapsed = !this.navCollapsed;
    if (this.navCollapsed && window.scrollY > 40) {
      this.hideLogo = true;
    } else {
      this.hideLogo = false;
    }
  }

  addClassFixedAndBg(): string {
    if (this.windowWidth < 992) {
      return 'position-fixed position-fixed-bg';
    } else {
      return '';
    }
  }

  ngOnDestroy(): void {
    if (this.dashboardStateSub) {
      this.dashboardStateSub.unsubscribe();
    }
  }
}
