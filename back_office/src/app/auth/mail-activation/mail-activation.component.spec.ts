import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MailActivationComponent } from './mail-activation.component';

describe('MailActivationComponent', () => {
  let component: MailActivationComponent;
  let fixture: ComponentFixture<MailActivationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MailActivationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MailActivationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
