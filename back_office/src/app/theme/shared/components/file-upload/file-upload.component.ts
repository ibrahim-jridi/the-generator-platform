import { Component, EventEmitter, Input, Output, ViewChild, ElementRef, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FileUploadComponent),
      multi: true
    }
  ]
})
export class FileUploadComponent implements ControlValueAccessor {
  @Input() public title: string = '';
  @Input() public subtitle: string = '';
  @Input() public displayPanelPhotoPlaceholder: string;
  @Input() public displayPanelPhoto: string;
  @Input() public allowedExtensions: string[] = [];
  @Input() public allowedMimeTypes: string[] = [];
  @Input() public showClearButton = true;
  @Output() public fileSelected = new EventEmitter<File>();
  @Output() public fileCleared = new EventEmitter<void>();

  @ViewChild('fileInput', { static: false }) fileInput: ElementRef;
  public file: File | null = null;
  public isLoading = false;
  public progressBarValue = 0;

  constructor(
    private toastService: AppToastNotificationService,
    private translatePipe: TranslatePipe
  ) { }

  private onChange: (value: File | null) => void = () => { };
  private onTouched: () => void = () => { };

  handleFileSelected(event: any): void {
    const fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      this.file = fileList[0];
      this.validateFile();
      this.onChange(this.file);
    }
  }

  handleDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  handleDrop(event: DragEvent): void {
    event.preventDefault();
    const files = event.dataTransfer?.files;
    if (files.length === 0) {
      this.toastService.onError(this.translatePipe.transform('configuration.error.file.empty-file'), this.translatePipe.transform('menu.ERROR'));
      return;
    }
    if (files.length > 0) {
      this.file = files[0];
      this.validateFile();
      this.onChange(this.file);
    }
  }

  private validateFile(): void {
    if (!this.file) {
      // Handle case where file is not selected
      return;
    }
    if (this.file?.size === 0) {
      this.toastService.onError(this.translatePipe.transform('configuration.error.file.empty-file'), this.translatePipe.transform('menu.ERROR'));
      return;
    }

    const fileExtension = this.file.name.split('.').pop().toLowerCase();
    const isMimeTypeValid = this.allowedMimeTypes?.length ? this.allowedMimeTypes.includes(this.file.type) : true;
    const isExtensionValid = this.allowedExtensions.includes('*') ;

    if (((!this.allowedExtensions.includes(fileExtension) || !isMimeTypeValid) )&&(!isExtensionValid)){
      this.toastService.onError(this.translatePipe.transform('configuration.error.format'), this.translatePipe.transform('menu.ERROR'));
      this.fileCleared.emit();
      this.onChange(null);
      this.clearSelectedFile();
    } else if(isExtensionValid) {
      this.isLoading = true;
      this.progressBarValue = 0;
      this.incrementProgress();
      this.fileSelected.emit(this.file);
    }else {
      this.isLoading = true;
      this.progressBarValue = 0;
      this.incrementProgress();
      this.fileSelected.emit(this.file);
    }
  }

  private incrementProgress(): void {
    const interval = 10;
    const totalIterations = 100;

    const increment = () => {
      this.progressBarValue++;
      if (this.progressBarValue < totalIterations) {
        setTimeout(increment, interval);
      } else {
        this.isLoading = false;
      }
    };

    setTimeout(increment, interval);
  }

  clearSelectedFile(): void {
    this.file = null;
    this.fileCleared.emit();
    this.onChange(null);
  }

  openFileInput(): void {
    this.fileInput.nativeElement.click();
  }

  // ControlValueAccessor implementation
  writeValue(value: File | null): void {
    this.file = value;
  }

  registerOnChange(fn: (value: File | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }
}
