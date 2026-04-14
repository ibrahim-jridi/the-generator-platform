import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PmUserUpdateProfileComponent } from './pm-user-update-profile.component';

describe('PmUserUpdateProfileComponent', () => {
  let component: PmUserUpdateProfileComponent;
  let fixture: ComponentFixture<PmUserUpdateProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PmUserUpdateProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PmUserUpdateProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
