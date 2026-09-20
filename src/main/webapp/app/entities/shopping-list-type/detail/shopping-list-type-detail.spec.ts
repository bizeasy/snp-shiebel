import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ShoppingListTypeDetail } from './shopping-list-type-detail';

describe('ShoppingListType Management Detail Component', () => {
  let comp: ShoppingListTypeDetail;
  let fixture: ComponentFixture<ShoppingListTypeDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./shopping-list-type-detail').then(m => m.ShoppingListTypeDetail),
              resolve: { shoppingListType: () => of({ id: 6209 }) },
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
    fixture = TestBed.createComponent(ShoppingListTypeDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load shoppingListType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ShoppingListTypeDetail);

      // THEN
      expect(instance.shoppingListType()).toEqual(expect.objectContaining({ id: 6209 }));
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
