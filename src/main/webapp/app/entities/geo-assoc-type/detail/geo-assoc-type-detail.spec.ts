import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { GeoAssocTypeDetail } from './geo-assoc-type-detail';

describe('GeoAssocType Management Detail Component', () => {
  let comp: GeoAssocTypeDetail;
  let fixture: ComponentFixture<GeoAssocTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./geo-assoc-type-detail').then(m => m.GeoAssocTypeDetail),
              resolve: { geoAssocType: () => of({ id: 9608 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GeoAssocTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load geoAssocType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GeoAssocTypeDetail);

      // THEN
      expect(instance.geoAssocType()).toEqual(expect.objectContaining({ id: 9608 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
