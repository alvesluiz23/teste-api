<script setup>
import { ref, toRaw } from 'vue'

const operacao = ref(0)
const carro = ref({
  id: null,
  veiculo: '',
  marca: '',
  cor: '',

  ano: 0,
  descricao: '',
  created: null,
  updated: null

})
const dados = ref(null)
const dadosAtualizacao = ref({
  id: null,
  veiculo: '',
  marca: '',
  cor: '',
  vendido: false,
  ano: 0,
  descricao: '',
  created: null,
  updated: null
}
)




async function buscarDados() {
  try {
    mudaOperacao(0)
    const response = await fetch('http://127.0.0.1:8080/veiculos')
    console.log(response.status)
    dados.value = await response.json()
  } catch (error) {
    console.error('Erro na API:', error)
  }
}

async function cadastrarDados() {
  try {
    carro.created = new Date().toLocaleString()
    carro.updated = new Date().toLocaleString()

    const resp = await fetch('http://localhost:8080/veiculos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(carro.value),
    })

  } catch (error) {
    console.error('Erro na API:', error)
  }
}

async function atualizaDados() {
  try {

    dadosAtualizacao.value.updated = new Date().toISOString()

    const id = dadosAtualizacao.value.id
    console.log(JSON.stringify(dadosAtualizacao.value))



    const resp = await fetch(`http://localhost:8080/veiculos?id=${id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(dadosAtualizacao.value),
    })

  } catch (error) {
    console.error('Erro na API:', error)
  }
}


function mudaOperacao(modoOperacao) {
  operacao.value = modoOperacao
}

function atualizaVeiculo(dado) {


  dadosAtualizacao.value = structuredClone(toRaw(dado))
  console.log(dadosAtualizacao.value.id)
  operacao.value = 2
}


function criaCarro() {
  operacao.value = 1
}
</script>

<template>
  <button @click="buscarDados()">Consulta carros</button>
  <button @click="mudaOperacao(1)">Cadastro</button>



  <div v-if="operacao === 0" class="wrap">
    <div class="grid">
      <article v-for="dado in dados" :key="dado.id" class="card">
        <header class="card-header">
          <h3 class="title">{{ dado.veiculo || dado.modelo }}</h3>
          <span class="id">#{{ dado.id }}</span>
        </header>

        <dl class="meta">
          <div>
            <dt>Marca</dt>
            <dd>{{ dado.marca }}</dd>
          </div>
          <div>
            <dt>Cor</dt>
            <dd><span class="pill">{{ dado.cor }}</span></dd>
          </div>
          <div>
            <dt>Ano</dt>
            <dd>{{ dado.ano }}</dd>
          </div>
        </dl>

        <p class="desc">{{ dado.descricao }}</p>

        <button @click="atualizaVeiculo(dado)">
          Atualizar
        </button>
      </article>
    </div>
  </div>


  <div v-if="operacao === 1" class="panel">

    <p>Cadastrar veiculos, entre com as informações do veiculo que você deseja cadastrar:</p>
    <form @submit.prevent="cadastrarDados()">
      <div class="form-row">
        <input v-model="carro.veiculo" required placeholder="Modelo" />
        <input v-model="carro.marca" required placeholder="marca" />
        <input v-model.number="carro.ano" type="number" required placeholder="ano" />
        <label class="check"><input v-model="carro.vendido" type="checkbox" /> Vendido</label>
        <input v-model="carro.descricao" required placeholder="descricao" class="span-2" />
        <input v-model="carro.cor" required placeholder="cor" />
        <div class="actions"><button class="btn primary">Cadastra carro</button></div>
      </div>
    </form>
  </div>


  <div v-if="operacao === 2" class="panel">
    <form @submit.prevent="atualizaDados()">
      <div class="form-row">
        <input v-model="dadosAtualizacao.veiculo" required placeholder="Modelo" />
        <input v-model="dadosAtualizacao.marca" placeholder="marca" />
        <input v-model.number="dadosAtualizacao.ano" type="number" required placeholder="ano" />
        <label class="check"><input v-model="dadosAtualizacao.vendido" type="checkbox" /> Vendido</label>
        <input v-model="dadosAtualizacao.descricao" required placeholder="descricao" class="span-2" />
        <input v-model="dadosAtualizacao.cor" required placeholder="cor" />
        <div class="actions"><button class="btn primary">Atualizar carro</button></div>
      </div>
    </form>
  </div>
</template>


<style scoped>
.panel {
  background: #111827;
  /* dark card */
  color: #e5e7eb;
  border: 1px solid #1f2937;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .25), 0 8px 18px rgba(0, 0, 0, .15);
  margin-top: 14px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px 14px;
  align-items: start;
}

.form-row input[type="text"],
.form-row input[type="number"],
.form-row input[type="search"],
.form-row input[type="password"],
.form-row input:not([type]),
.form-row textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #374151;
  border-radius: 10px;
  background: #0f172a;
  color: #e5e7eb;
  font-size: .95rem;
  outline: none;
  transition: border-color .15s ease, box-shadow .15s ease, background .15s ease;
}

.form-row input::placeholder,
.form-row textarea::placeholder {
  color: #9ca3af;
}

.form-row input:focus,
.form-row textarea:focus {
  border-color: #60a5fa;
  box-shadow: 0 0 0 3px rgba(96, 165, 250, .25);
  background: #0b1220;
}

.check {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border: 1px solid #374151;
  border-radius: 10px;
  background: #0f172a;
  height: 42px;
  color: #e5e7eb;
}

.span-2 {
  grid-column: span 2;
}

.actions {
  grid-column: 1 / -1;
  display: flex;
  justify-content: flex-end;
  margin-top: 6px;
}

.btn {
  appearance: none;
  border: 1px solid #1d4ed8;
  background: #2563eb;
  color: #fff;
  padding: 10px 14px;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  transition: background .15s ease, transform .06s ease, box-shadow .15s ease;
}

.btn:hover {
  background: #1d4ed8;
}

.btn:active {
  transform: translateY(1px);
}

.wrap {
  padding: 8px 0;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 14px;
}

.card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, .08), 0 8px 18px rgba(0, 0, 0, .04);
  transition: transform .15s ease, box-shadow .15s ease;
}

.card:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, .10), 0 10px 22px rgba(0, 0, 0, .06);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 8px;
}

.title {
  font-size: 1.05rem;
  font-weight: 600;
}

.id {
  font-size: .8rem;
  color: #6b7280;
}

.meta {
  display: grid;
  grid-template-columns: repeat(3, auto);
  gap: 6px 14px;
  margin: 6px 0 8px;
}

.meta dt {
  font-size: .72rem;
  color: #6b7280;
}

.meta dd {
  margin: 0;
  font-weight: 500;
}

.pill {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 999px;
  background: #f3f4f6;
}

.desc {
  color: #4b5563;
  line-height: 1.35;
  margin-top: 6px;
}
</style>
