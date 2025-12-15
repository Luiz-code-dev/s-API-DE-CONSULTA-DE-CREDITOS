import { Component, Input } from '@angular/core';
import { Credito } from '../../models/credito.model';

@Component({
  selector: 'app-credito-table',
  templateUrl: './credito-table.component.html',
  styleUrls: ['./credito-table.component.scss']
})
export class CreditoTableComponent {

  @Input() creditos: Credito[] = [];

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('pt-BR');
  }

  formatPercentage(value: number): string {
    return `${value.toFixed(2)}%`;
  }
}
