import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ShoppingListItemDetail } from './shopping-list-item-detail';

describe('ShoppingListItem Management Detail Component', () => {
  let comp: ShoppingListItemDetail;
  let fixture: ComponentFixture<ShoppingListItemDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./shopping-list-item-detail').then(m => m.ShoppingListItemDetail),
              resolve: { shoppingListItem: () => of({ id: 26675 }) },
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
    fixture = TestBed.createComponent(ShoppingListItemDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load shoppingListItem on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ShoppingListItemDetail);

      // THEN
      expect(instance.shoppingListItem()).toEqual(expect.objectContaining({ id: 26675 }));
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
