import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {SnotifyPosition, SnotifyService, SnotifyToastConfig} from "ng-alt-snotify";

@Injectable()
export class AppToastNotificationService {
  private timeout = 3000;
  private position: SnotifyPosition = SnotifyPosition.rightTop;
  private progressBar = true;
  private closeClick = true;
  private newTop = true;
  private filterDuplicates = false;
  private backdrop = -1;
  private dockMax = 80;
  private blockMax = 6;
  private pauseHover = true;
  private titleMaxLength = 15;
  private bodyMaxLength = 200;

  constructor(private snotifyService: SnotifyService) {
  }

  public onSuccess(body: string, title: string): any {
    this.snotifyService.success(body, title, this.getConfig());
  }

  public onInfo(body: string, title: string): any {
    this.snotifyService.info(body, title, this.getConfig());
  }

  public onError(body: string, title: string): any {
    this.snotifyService.error(body, title, this.getConfig());
  }

  public onWarning(body: string, title: string): any {
    this.snotifyService.warning(body, title, this.getConfig());
  }

  onSimple(body: string, title: string): any {
    const icon = `https://placehold.it/48x100`;

    this.snotifyService.simple(body, title, {
      ...this.getConfig(),
      icon
    });
  }

  public onAsyncLoading(bodyText: string, titleText: string, typeNotif: 'error' | 'succes' | 'succesWait'): any {
    const errorAction = new Observable((observer) => {
      setTimeout(() => {
        observer.error({
          title: titleText,
          body: bodyText
        });
      }, 2000);
    });

    const successAction = new Observable((observer) => {
      setTimeout(() => {
        observer.next({
          body: 'Still loading.....'
        });
      }, 2000);

      setTimeout(() => {
        observer.next({
          title: 'Success',
          body: 'Example. Data loaded!',
          config: {
            closeOnClick: true,
            timeout: 5000,
            showProgressBar: true
          }
        });
        observer.complete();
      }, 5000);
    });

    const {timeout, ...config} = this.getConfig();
    this.snotifyService.async('This will resolve with error', 'Async', errorAction, config);
    this.snotifyService.async('This will resolve with success', successAction, config);
    this.snotifyService.async(
      'Called with promise',
      'Error async',
      new Promise((resolve, reject) => {
        setTimeout(
          () =>
            reject({
              title: 'Error!!!',
              body: 'We got an example error!',
              config: {
                closeOnClick: true
              }
            }),
          1000
        );
        setTimeout(
          () =>
            resolve({
              title: 'Suceess!!!',
              body: 'We got an example!',
              config: {
                closeOnClick: true
              }
            }),
          1500
        );
      }),
      config
    );
  }

  onConfirmation(body: string, title: string): any {
    const {timeout, closeOnClick, ...config} = this.getConfig();
    this.snotifyService.confirm(body, title, {
      ...config,
      buttons: [
        {text: 'Yes', action: () => console.log('Clicked: Yes'), bold: false},
        {text: 'No', action: () => console.log('Clicked: No')},
        {
          text: 'Later',
          action: (toast) => {
            console.log('Clicked: Later');
            this.snotifyService.remove(toast.id);
          }
        },
        {
          text: 'Close',
          action: (toast) => {
            console.log('Clicked: Close');
            this.snotifyService.remove(toast.id);
          },
          bold: true
        }
      ]
    });
  }

  onPrompt(body: string, title: string): any {
    const {timeout, closeOnClick, ...config} = this.getConfig();
    this.snotifyService
    .prompt(body, title, {
      ...config,
      buttons: [
        {
          text: 'Yes',
          action: (toast) => console.log('Said Yes: ' + toast.value)
        },
        {
          text: 'No',
          action: (toast) => {
            console.log('Said No: ' + toast.value);
            this.snotifyService.remove(toast.id);
          }
        }
      ],
      placeholder: 'Enter "ng-snotify" to validate this input'
    })
    .on('input', (toast) => {
      console.log(toast.value);
      toast.valid = !!toast.value.match('ng-snotify');
    });
  }

  onHtml(html: string): any {
    this.snotifyService.html(html, this.getConfig());
  }

  onClear(): any {
    this.snotifyService.clear();
  }

  private getConfig(): SnotifyToastConfig {
    this.snotifyService.setDefaults({
      global: {
        newOnTop: this.newTop,
        maxAtPosition: this.blockMax,
        maxOnScreen: this.dockMax,
        filterDuplicates: this.filterDuplicates
      }
    });
    return {
      bodyMaxLength: this.bodyMaxLength,
      titleMaxLength: this.titleMaxLength,
      backdrop: this.backdrop,
      position: this.position,
      timeout: this.timeout,
      showProgressBar: this.progressBar,
      closeOnClick: this.closeClick,
      pauseOnHover: this.pauseHover
    };
  }
}
