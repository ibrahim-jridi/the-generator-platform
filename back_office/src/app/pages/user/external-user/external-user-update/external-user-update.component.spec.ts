import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalUserUpdateComponent } from './external-user-update.component';

describe('ExternalUserUpdateComponent', () => {
  let component: ExternalUserUpdateComponent;
  let fixture: ComponentFixture<ExternalUserUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExternalUserUpdateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExternalUserUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
