import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CreditoService } from '../../services/credito.service';
import { Credito } from '../../models/credito.model';

@Component({
  selector: 'app-consulta-credito',
  templateUrl: './consulta-credito.component.html',
  styleUrls: ['./consulta-credito.component.scss']
})
export class ConsultaCreditoComponent implements OnInit {

  searchForm!: FormGroup;
  creditos: Credito[] = [];
  loading = false;
  error: string | null = null;
  searched = false;

  searchTypes = [
    { value: 'nfse', label: 'Número da NFS-e' },
    { value: 'credito', label: 'Número do Crédito' }
  ];

  constructor(
    private fb: FormBuilder,
    private creditoService: CreditoService
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  private initForm(): void {
    this.searchForm = this.fb.group({
      searchType: ['nfse', Validators.required],
      searchValue: ['', [Validators.required, Validators.minLength(1)]]
    });
  }

  onSearch(): void {
    if (this.searchForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = null;
    this.creditos = [];
    this.searched = true;

    const { searchType, searchValue } = this.searchForm.value;

    if (searchType === 'nfse') {
      this.searchByNfse(searchValue);
    } else {
      this.searchByCredito(searchValue);
    }
  }

  private searchByNfse(numeroNfse: string): void {
    this.creditoService.findByNumeroNfse(numeroNfse).subscribe({
      next: (creditos) => {
        this.creditos = creditos;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  private searchByCredito(numeroCredito: string): void {
    this.creditoService.findByNumeroCredito(numeroCredito).subscribe({
      next: (credito) => {
        this.creditos = [credito];
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  onClear(): void {
    this.searchForm.reset({ searchType: 'nfse', searchValue: '' });
    this.creditos = [];
    this.error = null;
    this.searched = false;
  }

  get searchTypeLabel(): string {
    const type = this.searchForm.get('searchType')?.value;
    return type === 'nfse' ? 'NFS-e' : 'Crédito';
  }
}
