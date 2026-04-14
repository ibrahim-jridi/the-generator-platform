import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {TokenUtilsService} from '../../../shared/services/token-utils.service';
import {AppToastNotificationService} from '../../../shared/services/appToastNotification.service';
import {TranslatePipe} from '@ngx-translate/core';
import {NavigationExtras, Router} from '@angular/router';
import {CamundaService} from '../../../shared/services/camunda.service';
import {LoaderService} from "../../../shared/services/loader.service";

@Component({
  selector: 'app-service-panel',
  templateUrl: './service-panel.component.html',
  styleUrls: ['./service-panel.component.scss']
})
export class ServicePanelComponent implements OnInit {
  private _cardData: any;
  public userQualified: boolean = false;

  @Input()
  set cardData(value: any) {
    this._cardData = value;
    this.selectedService = value;
  }

  get cardData() {
    return this._cardData;
  }

  selectedService: any = null;
  selectedSubService: any = null;

  @ViewChild('carousel', { static: false }) carousel: ElementRef | undefined;

  constructor(
    private tokenUtilisService: TokenUtilsService,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private router: Router,
    private camundaService: CamundaService,
    private loaderService: LoaderService
  ) {}

  ngOnInit() {
    let profileCompleted = sessionStorage.getItem('profileCompleted');
    this.userQualified = !this.tokenUtilisService.isPM() && profileCompleted == "true";
    this.selectedService = this.cardData;
  }

  onSelectSubService(subService: any) {
    this.selectedSubService = subService;
  }

  click(processKey: string) {
    this.loaderService.show();
    this.camundaService.startProcess(processKey).subscribe(
      (res) => {
        this.camundaService.getTaskByProcessInstanceId(res.processInstanceId).subscribe(
          (data) => {
            this.loaderService.hide();
            const navigationExtras: NavigationExtras = {
              state: {
                processInstanceId: data.executionId,
                taskId: data.id
              }
            };
            this.router.navigate(['pages/task-management/task-list/validate-task', data.name], navigationExtras);
          },
          (error) => {
            this.loaderService.hide();
            this.toastrService.onError(this.translatePipe.transform('task.FAILED_TO_GET_TASK'), this.translatePipe.transform('menu.ERROR'));
          }
        );
        console.log(res);
      },
      (err) => {
        this.loaderService.hide();
        console.log(err);
      }
    );
  }

  scrollCarousel(direction: number) {
    if (this.carousel) {
      const scrollAmount = 200;
      this.carousel.nativeElement.scrollLeft += direction * scrollAmount;
    }
  }
  checkAuthorities(authority): boolean {
    return this.tokenUtilisService.hasUserRole(authority);
  }
}
