import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PmUserProfileComponent } from './pm-user-profile.component';

describe('PmUserProfileComponent', () => {
  let component: PmUserProfileComponent;
  let fixture: ComponentFixture<PmUserProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PmUserProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PmUserProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
