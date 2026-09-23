import { ChangeDetectionStrategy, Component, computed, forwardRef, inject, input, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

import { looksLikeId } from '../looks-like-id';
import { TermsService } from './terms.service';

/**
 * A terms box that suggests what has been used before.
 *
 * snp21 had bare text inputs here, and every order in the dev database ended up with a
 * number such as 105114 in Payment Terms, which then printed on the invoice. The list
 * offered comes from terms already saved on real orders rather than a made-up set, so it
 * stays free text while nudging towards consistent wording, and a value that is only a
 * long number is called out as it is typed rather than silently saved.
 */
@Component({
  selector: 'sys-terms-input',
  templateUrl: './terms-input.html',
  styleUrl: './terms-input.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => TermsInput),
      multi: true,
    },
  ],
})
export class TermsInput implements ControlValueAccessor {
  /** Which set of suggestions to show. */
  readonly kind = input<'paymentTerms' | 'deliveryTerms'>('paymentTerms');
  readonly inputId = input<string>('terms');
  readonly rows = input<number>(2);
  readonly placeholder = input<string>('');

  readonly value = signal('');
  readonly disabled = signal(false);
  readonly options = signal<string[]>([]);

  /** A bare four-digit-or-longer number is an id, not terms. */
  readonly looksLikeAnId = computed(() => looksLikeId(this.value()));

  readonly listId = computed(() => `${this.inputId()}-suggestions`);

  private readonly termsService = inject(TermsService);
  private onChange: (value: string) => void = () => undefined;
  private onTouched: () => void = () => undefined;

  constructor() {
    this.termsService.suggestions().subscribe({
      next: all => this.options.set(all[this.kind()] ?? []),
      error: () => this.options.set([]),
    });
  }

  writeValue(value: string | null): void {
    this.value.set(value ?? '');
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled.set(isDisabled);
  }

  update(value: string): void {
    this.value.set(value);
    this.onChange(value);
  }

  touched(): void {
    this.onTouched();
  }
}
