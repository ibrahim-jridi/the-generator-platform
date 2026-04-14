import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TranslationUploadComponent } from './translation-upload.component';

describe('TranslationUploadComponent', () => {
  let component: TranslationUploadComponent;
  let fixture: ComponentFixture<TranslationUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TranslationUploadComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TranslationUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
