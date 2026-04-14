import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalUserViewComponent } from './external-user-view.component';

describe('ExternalUserViewComponent', () => {
  let component: ExternalUserViewComponent;
  let fixture: ComponentFixture<ExternalUserViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExternalUserViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExternalUserViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
