import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SensorEditAddComponent } from './sensor-edit-add.component';

describe('SensorEditAddComponent', () => {
  let component: SensorEditAddComponent;
  let fixture: ComponentFixture<SensorEditAddComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SensorEditAddComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SensorEditAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
