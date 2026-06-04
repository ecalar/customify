import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';

// Configuración del servidor API
const API_BASE_URL = 'http://localhost:8080';

// Estado global de la aplicación
let productData = null;
let currentConfiguration = {};
let basePrice = 0;
let loadedModel = null;

// Elementos del DOM
const container = document.getElementById('viewer-container');
const loadingSpinner = document.getElementById('loading-spinner');
const saveBtn = document.getElementById('save-config');
const optionsContainer = document.getElementById('options-container');

// --- 1. CONFIGURACIÓN BÁSICA DE THREE.JS ---
const scene = new THREE.Scene();
// Color de fondo transparente para que se vea el CSS oscuro
scene.background = null;

const camera = new THREE.PerspectiveCamera(45, container.clientWidth / container.clientHeight, 0.1, 100);
camera.position.set(3, 2, 4);

const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
renderer.setSize(container.clientWidth, container.clientHeight);
renderer.setPixelRatio(window.devicePixelRatio);
// Mejorar renderizado de colores e iluminación
renderer.toneMapping = THREE.ACESFilmicToneMapping;
renderer.toneMappingExposure = 1.0;
container.appendChild(renderer.domElement);

const controls = new OrbitControls(camera, renderer.domElement);
controls.enableDamping = true;
controls.dampingFactor = 0.05;

// Iluminación
const ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
scene.add(ambientLight);

const directionalLight = new THREE.DirectionalLight(0xffffff, 1.5);
directionalLight.position.set(5, 10, 7);
scene.add(directionalLight);

// Bucle de renderizado
function animate() {
    requestAnimationFrame(animate);
    controls.update();
    renderer.render(scene, camera);
}
animate();

// Ajustar canvas al redimensionar la ventana
window.addEventListener('resize', () => {
    camera.aspect = container.clientWidth / container.clientHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(container.clientWidth, container.clientHeight);
});

// --- 2. LÓGICA DE NEGOCIO (CUSTOMIFY) ---

// Obtenemos el ID del producto de la URL (ej: index.html?id=1)
// Si no hay, forzamos el 1 para pruebas.
const urlParams = new URLSearchParams(window.location.search);
const productId = urlParams.get('id') || 1;

async function initViewer() {
    try {
        // 1. Obtener datos del producto
        const response = await fetch(`${API_BASE_URL}/public/products/${productId}`);
        if (!response.ok) throw new Error('Producto no encontrado');

        productData = await response.json();
        basePrice = productData.basePrice;

        // 2. Actualizar UI básica
        document.getElementById('product-name').textContent = productData.name;
        document.getElementById('product-description').textContent = productData.description || '';

        // 3. Cargar opciones dinámicas
        buildOptionsUI();
        updatePrice();

        // 4. Cargar modelo 3D
        loadModel(productData.modelPath);

    } catch (error) {
        console.error("Error inicializando visor:", error);
        loadingSpinner.textContent = "Error al cargar el producto.";
    }
}

function buildOptionsUI() {
    if (!productData.options || productData.options.length === 0) {
        optionsContainer.innerHTML = '<p class="text-sm text-muted">Este producto no tiene opciones personalizables.</p>';
        saveBtn.disabled = false;
        return;
    }

    productData.options.forEach(option => {
        // Guardamos el valor por defecto en el estado
        currentConfiguration[option.name] = option.defaultValue || option.choices[0];

        const groupDiv = document.createElement('div');
        groupDiv.className = 'option-group';

        const label = document.createElement('label');
        label.textContent = `${option.name} (+${option.priceSupplement}€)`;

        const select = document.createElement('select');
        select.dataset.optionId = option.id;
        select.dataset.optionName = option.name;
        select.dataset.price = option.priceSupplement;
        select.dataset.type = option.type;

        option.choices.forEach(choice => {
            const optElement = document.createElement('option');
            optElement.value = choice;
            optElement.textContent = choice;
            if (choice === currentConfiguration[option.name]) optElement.selected = true;
            select.appendChild(optElement);
        });

        // Evento al cambiar una opción
        select.addEventListener('change', (e) => {
            const val = e.target.value;
            const optName = e.target.dataset.optionName;
            const optType = e.target.dataset.type;

            currentConfiguration[optName] = val;
            updatePrice();
            applyMaterialChange(optType, val);
        });

        groupDiv.appendChild(label);
        groupDiv.appendChild(select);
        optionsContainer.appendChild(groupDiv);
    });

    saveBtn.disabled = false;
}

function updatePrice() {
    let total = basePrice;

    // Sumar suplementos de opciones que no tengan su valor por defecto inicial
    const selects = document.querySelectorAll('#options-container select');
    selects.forEach(select => {
        // Lógica simplificada: si la opción seleccionada tiene suplemento > 0, se suma.
        // En una app real, el suplemento podría ir por cada choice individual.
        // Aquí asumimos que activar la opción aplica el suplemento global de la opción.
        const priceSup = parseFloat(select.dataset.price);
        if (priceSup > 0) total += priceSup;
    });

    document.getElementById('total-price').textContent = `${total.toFixed(2)} €`;
    return total;
}

function loadModel(modelFilename) {
    const loader = new GLTFLoader();
    const modelUrl = `${API_BASE_URL}/uploads/${modelFilename}`;

    loader.load(
        modelUrl,
        (gltf) => {
            loadedModel = gltf.scene;

            // Centrar el modelo
            const box = new THREE.Box3().setFromObject(loadedModel);
            const center = box.getCenter(new THREE.Vector3());
            loadedModel.position.x += (loadedModel.position.x - center.x);
            loadedModel.position.y += (loadedModel.position.y - center.y);
            loadedModel.position.z += (loadedModel.position.z - center.z);

            scene.add(loadedModel);
            loadingSpinner.style.display = 'none';

            // Aplicar configuraciones por defecto al cargar
            const selects = document.querySelectorAll('#options-container select');
            selects.forEach(select => {
                applyMaterialChange(select.dataset.type, select.value);
            });
        },
        (xhr) => {
            const percent = Math.round((xhr.loaded / xhr.total) * 100);
            if(percent > 0) loadingSpinner.textContent = `Cargando... ${percent}%`;
        },
        (error) => {
            console.error('Error cargando GLTF', error);
            loadingSpinner.textContent = "Error al cargar el modelo 3D.";
        }
    );
}

// Cambiar aspecto del modelo 3D
function applyMaterialChange(type, value) {
    if (!loadedModel) return;

    loadedModel.traverse((child) => {
        if (child.isMesh) {
            // Nota: En un entorno real, buscarías child.name === "NombrePieza"
            // Para este MVP, si es tipo COLOR, pintamos todo el objeto para que se note el efecto.
            if (type === 'COLOR') {
                // Clonamos el material para no afectar a otros objetos que compartan material
                child.material = child.material.clone();
                try {
                    child.material.color.set(value);
                } catch(e) {
                    console.warn("Formato de color inválido:", value);
                }
            }
        }
    });
}

// --- 3. GUARDAR CONFIGURACIÓN ---
saveBtn.addEventListener('click', async () => {
    saveBtn.disabled = true;
    saveBtn.textContent = 'Guardando...';
    const statusMsg = document.getElementById('status-msg');
    statusMsg.className = 'hidden';

    const payload = {
        productId: parseInt(productId),
        data: JSON.stringify(currentConfiguration),
        totalPrice: updatePrice()
    };

    try {
        const res = await fetch(`${API_BASE_URL}/public/configurations`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            statusMsg.textContent = '¡Configuración guardada! ID de pedido generado.';
            statusMsg.className = 'msg-success';
        } else {
            throw new Error();
        }
    } catch (error) {
        statusMsg.textContent = 'Error al guardar la configuración.';
        statusMsg.className = 'msg-error';
    } finally {
        saveBtn.disabled = false;
        saveBtn.textContent = 'Guardar configuración';
    }
});

// Iniciar aplicación
initViewer();