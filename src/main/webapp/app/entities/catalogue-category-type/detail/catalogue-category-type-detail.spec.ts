import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CatalogueCategoryTypeDetail } from './catalogue-category-type-detail';

describe('CatalogueCategoryType Management Detail Component', () => {
  let comp: CatalogueCategoryTypeDetail;
  let fixture: ComponentFixture<CatalogueCategoryTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./catalogue-category-type-detail').then(m => m.CatalogueCategoryTypeDetail),
              resolve: { catalogueCategoryType: () => of({ id: 27421 }) },
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
    fixture = TestBed.createComponent(CatalogueCategoryTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load catalogueCategoryType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CatalogueCategoryTypeDetail);

      // THEN
      expect(instance.catalogueCategoryType()).toEqual(expect.objectContaining({ id: 27421 }));
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
