import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalUserAddComponent } from './external-user-add.component';

describe('ExternalUserAddComponent', () => {
  let component: ExternalUserAddComponent;
  let fixture: ComponentFixture<ExternalUserAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExternalUserAddComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExternalUserAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
