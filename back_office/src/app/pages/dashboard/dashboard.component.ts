import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { TokenUtilsService } from '../../shared/services/token-utils.service';
import { DashboardService } from 'src/app/shared/services/dashboard.service';
import { CamundaService } from '../../shared/services/camunda.service';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';
import { TranslatePipe } from '@ngx-translate/core';
import { Router } from '@angular/router';
import {LoginService} from "../../shared/services/login.service";
import decode from "jwt-decode";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  @ViewChild('carousel', { static: false }) carousel: ElementRef | undefined;
  public username: any;
  public cardsData: any[] = [];
  public filteredCards: any[] = [];
  public selectedCard: any = null;
  public profileCompleted: boolean = false;
  public isExternalUser: boolean = false;
  public isAdmin: boolean = false;
  public fadeInEffect: boolean = false;
  token: any;

  constructor(
    private tokenUtilisService: TokenUtilsService,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private router: Router,
    private camundaService: CamundaService,
    private dashboardService: DashboardService,
    private tokenUtilsService: TokenUtilsService,
  private loginService: LoginService,

) {}

  ngOnInit() {

    this.refreshTokenManually();
    this.username = this.tokenUtilisService.getUsername();
    this.isExternalUser = this.tokenUtilisService.isUserExternal();
    this.isAdmin = this.tokenUtilsService.isAdmin;
    let profileCompleted = sessionStorage.getItem('profileCompleted');
    this.filterCards();
    if (profileCompleted != null && profileCompleted === 'true') {
      this.profileCompleted = true;
    } else if (profileCompleted != null && profileCompleted === 'false') {
      this.profileCompleted = false;
    } else {
      this.profileCompleted = null;
    }
    this.dashboardService.setDashboardState(true);
  }
  private refreshTokenManually() {
    const refreshToken = this.loginService.getRefreshToken(); // Assuming you have this
    if (!refreshToken) {
      console.warn('No refresh token available!');
      return;
    }

    this.loginService.refreshToken().subscribe({
      next: (newAccessToken: string) => {
        const decodedToken: any = decode(newAccessToken);
        sessionStorage.setItem('profileCompleted',decodedToken?.profile_completed);
        console.log('Token refreshed successfully:', newAccessToken);
      },
      error: (err) => {
        console.error('Token refresh failed:', err);
        this.loginService.logout();
      }
    });
  }
  filterCards() {
    this.cardsData = this.dashboardService.getCardsData();
    if (this.isAdmin) {
      this.filteredCards = this.cardsData.filter((card) => card.id == 'users' || card.id == 'process' || card.id == 'instance' || card.id == 'task');
    } else {
      this.filteredCards = this.cardsData.filter(
        (card) =>
          card.id == 'creating-entities' ||
          card.id == 'licenses' ||
          card.id == 'list-management' ||
          card.id == 'pharmacy-management' ||
          card.id == 'wholesaler-management'
      );
    }
    if (this.filteredCards && this.filteredCards.length > 0) {
      this.selectedCard = this.filteredCards[0];
    }
  }

  public onCardClick(card: any) {
    if (this.selectedCard !== card) {
      this.selectedCard = card;

      this.fadeInEffect = false;
      setTimeout(() => {
        this.fadeInEffect = true;
      }, 0);
    }
  }

  scrollCarousel(direction: number) {
    if (this.carousel) {
      const scrollAmount = 200;
      this.carousel.nativeElement.scrollLeft += direction * scrollAmount;
    }
  }

  ngOnDestroy(): void {
    this.dashboardService.setDashboardState(false);
  }
}
